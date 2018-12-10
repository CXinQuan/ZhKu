package com.xyb.zhku.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 陈鑫权  on 2018/10/27.
 */
public class ZipUtils {

    private static byte[] buffer = new byte[1024 * 10];

    private ZipUtils() {
        // empty
    }

    /**
     * 压缩文件
     *
     * @param filePath 待压缩的文件路径
     * @return 压缩后的文件
     */
    public static File zip(String filePath, FinishListener listener) {  //  listener 监听是否结束
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName); // 压缩后的target文件，放在
            if (target.exists()) {// 如果存在该压缩文件，则删除
                target.delete();
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos)); // 压缩后输出 到  target 路径
                // 添加对应的文件Entry
                addEntry(target,listener, "/", source, zos);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                //   IOUtil.closeQuietly(zos, fos);
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return target;
    }

    /**
     * 扫描添加文件Entry
     *
     * @param base   基路径
     * @param source 源文件
     * @param zos    Zip文件输出流
     * @throws IOException
     */
    private static void addEntry(File target,FinishListener listener, String base, File source, ZipOutputStream zos)
            throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            for (File file : source.listFiles()) {  //  将该 文件夹 下 的文件一个个 写入到 该 该压缩 输出流 中 形成一个 压缩包
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(null,null, entry + "/", file, zos);
            }
            //  一个个写完之后即完成  , 只需要第一次传进来的 listener 即可
            deleteFile(source);  // 压缩完成之后将该文件夹或者文件进行删除
            if (listener != null) {   // 传一个监听给外界，告诉外界自己已经压缩完成了
                listener.onfinish(target);
            }

        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } finally {
                //   IOUtil.closeQuietly(bis, fis);
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     *  flie：要删除的文件夹的所在位置  删除文件或者文件夹
     */
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public interface FinishListener {
        void onfinish(File target);
    }

//
//    /**
//     * 解压文件
//     *
//     * @param filePath 压缩文件路径
//     */
//    public static void unzip(String filePath) {
//        File source = new File(filePath);
//        if (source.exists()) {
//            ZipInputStream zis = null;
//            BufferedOutputStream bos = null;
//            try {
//                zis = new ZipInputStream(new FileInputStream(source));
//                ZipEntry entry = null;
//                while ((entry = zis.getNextEntry()) != null
//                        && !entry.isDirectory()) {
//
//                    File target = new File(source.getParent(), entry.getName());
//                    if (!target.exists()) {
//                        // 创建文件父目录
//                        (new File(target.getParent())).mkdirs();
//                    }
//                    // 写入文件
//                    bos = new BufferedOutputStream(new FileOutputStream(target));
//                    int read = 0;
//                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
//                        bos.write(buffer, 0, read);
//                    }
//                }
//                zis.closeEntry();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                IOUtil.closeQuietly(zis, bos);
//            }
//        }
//    }

    /**
     * 压缩某个文件
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void zip(String src, String dest) throws IOException {
        //定义压缩输出流
        ZipOutputStream out = null;
        try {
            //传入源文件
            File fileOrDirectory = new File(src);
            File outFile = new File(dest);
            //传入压缩输出流
            //创建文件前几级目录
            if (!outFile.exists()) {
                File parentfile = outFile.getParentFile();
                if (!parentfile.exists()) {
                    parentfile.mkdirs();
                }
            }
            //可以通过createNewFile()函数这样创建一个空的文件，也可以通过文件流的使用创建
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //判断是否是一个文件或目录
            //如果是文件则压缩
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                //否则列出目录中的所有文件递归进行压缩

                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    zipFileOrDirectory(out, entries[i], fileOrDirectory.getName() + "/");//传入最外层目录名

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩文件 或者 文件夹
     *
     * @param out
     * @param fileOrDirectory
     * @param curPath
     * @throws IOException
     */
    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath) throws IOException {
        FileInputStream in = null;
        try {
            //判断是否为目录
            if (!fileOrDirectory.isDirectory()) {
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);//读目录中的子项
                //归档压缩目录
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());//压缩到压缩目录中的文件名字
                //getName() 方法返回的路径名的名称序列的最后一个名字，这意味着表示此抽象路径名的文件或目录的名称被返回。
                //将压缩目录写到输出流中
                out.putNextEntry(entry);//out是带有最初传进的文件信息，一直添加子项归档目录信息
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                //列出目录中的所有文件
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    //递归压缩
                    zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");//第一次传入的curPath是空字符串
                }//目录没有后缀所以直接可以加"/"
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
