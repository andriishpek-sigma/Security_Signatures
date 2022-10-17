package data.cipher.rc5;

import data.random.CustomRandomGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RC5Utility {

    private final int sizeOfPartInBit;
    private final int sizeOfPartInByte;

    private byte[] initVector;

    public RC5Utility(int wordSize) {
        sizeOfPartInBit = wordSize;
        sizeOfPartInByte = sizeOfPartInBit / 8;

        // get random numbers and put them into byte array
        Long[] tmpLong = CustomRandomGenerator.invoke().nextSequence(8);
        int[] tmp = new int[8];
        for (int i = 0; i < tmpLong.length; i++) {
            tmp[i] = tmpLong[i].intValue();
        }
        this.initVector = new byte[8];
        for (int i = 0; i < tmp.length; i++) {
            this.initVector[i] = (byte) tmp[i];
        }
    }

    private int[] divisionIntoParts(byte[] data) {
        int[] tmp = new int[2];
        int n = data.length;
        int nB = n / 2;
        int nA = n - nB;
        int j;
        byte[] a = new byte[sizeOfPartInBit];
        byte[] b = new byte[sizeOfPartInBit];
        n--;
        j = sizeOfPartInBit - 1;
        for (int i = 0; i < nB; i++) {
            b[j] = data[n];
            n--;
            j--;
        }
        j = sizeOfPartInBit - 1;
        for (int i = nA - 1; i >= 0; i--) {
            a[j] = data[n];
            n--;
            j--;
        }
        tmp[0] = new BigInteger(a).intValue();
        tmp[1] = new BigInteger(b).intValue();
        return tmp;
    }


    private byte[] assemblingParts(int a, int b) {
        byte[] tmp = new byte[2 * sizeOfPartInByte];
        byte[] byteA = new BigInteger("" + a).toByteArray();
        byte[] byteB = new BigInteger("" + b).toByteArray();

//        byte[] byteAA = new byte[4];
//        byte[] byteBB = new byte[4];
//        IntByteConversion_extKt.toBytes(a, byteAA, 0);
//        IntByteConversion_extKt.toBytes(b, byteBB, 0);

        for (int i = 0; i < sizeOfPartInByte; i++) {
            tmp[i] = byteA[i];
            tmp[sizeOfPartInByte + i] = byteB[i];
        }
        return tmp;
    }


    private byte[] encryptBlock(byte[] data, RC5Key key) {
        int a;
        int b;
        int number;
        int[] s = key.getWords();
        int[] parts = divisionIntoParts(data);
        a = parts[0];
        b = parts[1];
        /*_____________________________________*/
        a = a + s[0];
        b = b + s[1];
        for (int i = 1; i <= key.getNumberOfRounds(); i++) {
            a = a ^ b;
            number = b % sizeOfPartInBit;
            a = Integer.rotateLeft(a, number);
            a = a + s[2 * i];
            b = b ^ a;
            number = a % sizeOfPartInBit;
            b = Integer.rotateLeft(b, number);
            b = b + s[2 * i + 1];
        }
        /*_____________________________________*/
        byte[] outputData = assemblingParts(a, b);
        return outputData;
    }


    private byte[] decryptBlock(byte[] data, RC5Key key) {
        int a;
        int b;
        int number;
        int[] s = key.getWords();
        int[] parts = divisionIntoParts(data);
        a = parts[0];
        b = parts[1];
        /*_____________________________________*/
        for (int i = key.getNumberOfRounds(); i != 0; i--) {
            number = a % sizeOfPartInBit;
            b = b - s[2 * i + 1];
            b = Integer.rotateRight(b, number);
            b = b ^ a;
            number = b % sizeOfPartInBit;
            a = a - s[2 * i];
            a = Integer.rotateRight(a, number);
            a = a ^ b;
        }
        a = a - s[0];
        b = b - s[1];
        /*_____________________________________*/
        byte[] outputData = assemblingParts(a, b);
        return outputData;
    }


    private List<byte[]> divisionIntoBlocks(byte[] data) {
        int n = data.length;
        int sizeOfBlock = 2 * sizeOfPartInByte;
        int divBlock = n > sizeOfBlock ? n % sizeOfBlock : sizeOfBlock - n;
        if (divBlock != 0) {
            System.out.println("Data size must be a multiple of " + sizeOfBlock);
        }
        int numbersOfBlocks = n / sizeOfBlock;
        byte[] tmp;
        int counter = 0;
        List<byte[]> parts = new ArrayList<>();
        for (int i = 0; i < numbersOfBlocks; i++) {
            tmp = new byte[sizeOfBlock];
            for (int j = 0; j < sizeOfBlock; j++) {
                tmp[j] = data[counter];
                counter++;
            }
            parts.add(tmp);
        }
        return parts;
    }


    private byte[] assemblyOfBlocks(List<byte[]> blocks) {
        int sizeOfBlock = 2 * sizeOfPartInByte;
        int n = blocks.size() * sizeOfBlock;
        byte[] outputData = new byte[n];
        int counter = 0;
        for (byte[] block : blocks) {
            for (int i = 0; i < sizeOfBlock; i++) {
                outputData[counter] = block[i];
                counter++;
            }
        }
        return outputData;
    }


    public byte[] encrypt(byte[] data, RC5Key key) {
        List<byte[]> inputBlocks = divisionIntoBlocks(data);
        List<byte[]> outputBlocks = new ArrayList<>();
        byte[] tmp;
        byte[] prevBlock = this.initVector.clone();
        for (byte[] block : inputBlocks) {
            tmp = encryptBlock(block, key);
            for (int i = 0; i < 8; i++) {
                prevBlock[i] = tmp[i];
            }
            outputBlocks.add(tmp);
        }
        byte[] outputData = assemblyOfBlocks(outputBlocks);
        return outputData;
    }


    public byte[] decrypt(byte[] data, RC5Key key) {
        List<byte[]> inputBlocks = divisionIntoBlocks(data);
        List<byte[]> outputBlocks = new ArrayList<>();
        byte[] tmp;
        byte[] prevBlock = this.initVector.clone();
        for (byte[] block : inputBlocks) {
            tmp = decryptBlock(block, key);
            for (int i = 0; i < 8; i++) {
                prevBlock[i] = tmp[i];
            }
            outputBlocks.add(tmp);
        }

        byte[] outputData = assemblyOfBlocks(outputBlocks);
        return outputData;
    }

    public byte[] getInitVector() {
        return this.initVector;
    }

    public void setInitVector(byte[] vector) {
        this.initVector = vector;
    }
}
