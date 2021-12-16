package data;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class UFile<T extends IRecord<T>> {

    private RandomAccessFile file;
    private RandomAccessFile fileFS;
    private Class<T> gClass;

    public PriorityQueue<Long> queueDeleted = new PriorityQueue<>();
    public PriorityQueue<Long> queueDeletedReverse = new PriorityQueue<>(Comparator.reverseOrder());

    public UFile(String file, String fileFS, Class paramClass) {
        gClass = paramClass;
        try {
            this.file = new RandomAccessFile(file, "rw");
            this.fileFS = new RandomAccessFile(fileFS, "rw");
            if (this.fileFS.length() != 0) {
                loadFS();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public long insert(T data) {

        long address;
        data.setValid(true);
        try {
            /*file.seek(file.length());
            address = file.getFilePointer();*/
            if (queueDeleted.size() == 0) {
                file.seek(file.length());
                address = file.getFilePointer();
            } else {
                address = queueDeleted.peek();
                file.seek(address);
                queueDeleted.remove(address);
                queueDeletedReverse.remove(address);
            }
            data.setAddress(address);
            file.write(data.toByteArray());
            saveFreeSpaces();
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long insertAtAddress(T data, long address) {

        data.setValid(true);
        try {
            file.seek(address);
            data.setAddress(file.getFilePointer());
            file.write(data.toByteArray());
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public T find(long address) {

        T test = null;

        try {
            test = (T) gClass.newInstance().createClass();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        byte[] blockBytes = new byte[test.getSize()];

        try {
            file.seek(address);
            file.read(blockBytes);

        } catch (IOException e) {
            System.out.println("PROBLEM");
            e.printStackTrace();
        }
        test.fromByteArray(blockBytes);

        return test;
    }


    public boolean delete(long address) {

        T test = null;

        try {
            test = (T) gClass.newInstance().createClass();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //byte[] blockBytes = new byte[test.getSize()];

        long deleteAdd = address;
        try {
            long length = file.length() - test.getSize();
            while (true) {
                if (deleteAdd == length) {

                    queueDeletedReverse.remove(deleteAdd);
                    queueDeleted.remove(deleteAdd);
                    if (queueDeletedReverse.size() != 0 && length - test.getSize() == queueDeletedReverse.peek()) {
                        length -= test.getSize();
                        deleteAdd = queueDeletedReverse.peek();
                    } else {
                        file.setLength(deleteAdd);
                        break;
                    }
                } else {
                    file.seek(address);
                    test.setValid(false);
                    file.write(test.toByteArray());

                    queueDeletedReverse.add(deleteAdd);
                    queueDeleted.add(deleteAdd);
                    break;
                }
            }
            saveFreeSpaces();
            return true;
        } catch (IOException e) {
            System.out.println(address + " chyba");
            e.printStackTrace();
        }
        return false;
    }


    public void clearFile() {
        //System.out.println(queueDeleted.size() + " volne miesta na konci");
        try {
            this.file.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFS() {

        try {
            byte[] bytes = new byte[(int) fileFS.length()];
            long deleted = 0;
            //System.out.println((fileMem.length() - 4) / Long.BYTES + " load miest");
            fileFS.read(bytes);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            int sizeDel = dataInputStream.readInt();
            for (int i = 0; i < sizeDel; i++) {
                deleted = dataInputStream.readLong();
                queueDeleted.add(deleted);
                queueDeletedReverse.add(deleted);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveFreeSpaces() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            this.fileFS.setLength(0);
            this.fileFS.seek(0);
            dataOutputStream.writeInt(queueDeleted.size());

            for (Long address: queueDeleted) {
                dataOutputStream.writeLong(address);
            }

            fileFS.write(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLength() {
        try {
            return this.file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getFreeSpaceFileLength() {
        return queueDeleted.size();
    }

    public void clearFiles() {
        try {
            this.file.setLength(0);
            this.fileFS.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTreeData() {
        long address = -100;
        try {
            file.seek(0);
            file.writeLong(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateTreeData(long rootAdd) {
        try {
            file.seek(0);
            file.writeLong(rootAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long loadTreeData() {

        try {
            file.seek(0);
            return file.readLong();
        } catch (IOException e) {
            System.out.println("PROBLEM");
            e.printStackTrace();
        }
        return -100;
    }

    public ArrayList<T> sequentialListing(int offset) {
        ArrayList<T> tests = new ArrayList<>();
        T test = null;

        try {
            test = (T) gClass.newInstance().createClass();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        byte[] blockBytes = new byte[test.getSize()];

        try {
            int position = 0 + offset;
            int count = 0;
            while (position < file.length()) {
                file.seek(position);
                file.read(blockBytes);
                test.fromByteArray(blockBytes);
                tests.add(test);
                test = (T) gClass.newInstance().createClass();
                position += test.getSize();
                count++;
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return tests;
    }


    public ArrayList<T> sequentialListingFS(int offset) {
        ArrayList<T> tests = new ArrayList<>();
        T test = null;

        try {
            System.out.println(fileFS.length() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            test = (T) gClass.newInstance().createClass();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        byte[] blockBytes = new byte[test.getSize()];

        try {
            int position = 4 + offset;
            int count = 0;
            while (position < fileFS.length()) {
                fileFS.seek(position);
                //System.out.println(count + " " + position + " pozicia");
                fileFS.read(blockBytes);
                test.fromByteArray(blockBytes);
                tests.add(test);
                test = (T) gClass.newInstance().createClass();
                position += 8;
                count++;
            }
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return tests;
    }
}
