package com.photo.photo.controller;


import com.photo.photo.utils.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/spider")
public class SpiderController {

//    public static String path = "https://www.duitang.com/search/?kw=%E6%98%A5%E5%A4%A9&type=feed";
    public static int num = -1,sum = 0;
    /**
     * 定义四个文件类（链接存储，图片储存，文件存储，错误链接存储）
     */
    public static File aLinkFile,imgLinkFile,docLinkFile,errorLinkFile;
    /**
     *
     * @param path		目标地址
     */
    public static void getAllLinks(String path){
        Document doc = null;
        try{
            doc = Jsoup.parse(HttpUtil.get(path));
        }catch (Exception e){
            //接收到错误链接（404页面）
            writeTxtFile(errorLinkFile, path+"\r\n");	//写入错误链接收集文件
            num++;
            if(sum>num){	//如果文件总数（sum）大于num(当前坐标)则继续遍历
                getAllLinks(getFileLine(aLinkFile, num));
            }
            return;
        }

        Elements imgLinks = doc.select("img[src]");

        //同时抓取该页面图片链接
        for(Element element:imgLinks){
            String srcStr = element.attr("src");
            if(!srcStr.contains("http://")&&!srcStr.contains("https://")){//没有这两个头
                srcStr = path+srcStr;
            }
            if(!readTxtFile(imgLinkFile).contains(srcStr)){
                //将图片链接写进文件中
                writeTxtFile(imgLinkFile, srcStr+"\r\n");
            }
        }
        num++;
        if(sum>num){
            getAllLinks(getFileLine(aLinkFile, num));
        }



    }

    /**
     * 读取文件内容
     * @param file	文件类
     * @return	文件内容
     */
    public static String readTxtFile(File file){
        String result = "";		//读取結果
        String thisLine = "";	//每次读取的行
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                while((thisLine=reader.readLine())!=null){
                    result += thisLine+"\n";
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入内容
     * @param file	文件类
     * @param urlStr	要写入的文本
     */
    public static void writeTxtFile(File file,String urlStr){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write(urlStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件指定行数的数据，用于爬虫获取当前要爬的链接
     * @param file	目标文件
     * @param num	指定的行数
     */
    public static String getFileLine(File file,int num){
        String thisLine = "";
        int thisNum = 0 ;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((thisLine = reader.readLine())!=null){
                if(num == thisNum){
                    return thisLine;
                }
                thisNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取文件总行数（有多少链接）
     * @param file	文件类
     * @return	总行数
     */
    public static int getFileCount(File file){
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(reader.readLine()!=null){	//遍历文件行
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<String> getFileContext(String path) throws Exception {
        FileReader fileReader =new FileReader(path);
        BufferedReader bufferedReader =new BufferedReader(fileReader);
        List<String> list =new ArrayList<String>();
        String str=null;
        while((str=bufferedReader.readLine())!=null) {
            if(str.trim().length()>2) {
                list.add(str);
            }
        }
        return list;
    }


    @GetMapping(value = "/{tag}")
    public static List<String> searchOnline(@PathVariable("tag") String tag) throws Exception {
        String path = "https://www.duitang.com/search/?kw="+tag+"&type=feed";
        String filePath = "/Library/Storage/2017/PhotoCloud/txt/";
        File fileDir = new File(filePath);                                                      //如果没有指定目录 新建一个文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();

        aLinkFile = new File(filePath + "ALinks.txt");
        imgLinkFile = new File(filePath + "ImgLinks.txt");

        //用数组存储文件对象，方便进行相同操作
        File[] files = new File[]{aLinkFile,imgLinkFile};
        try {
            for(File file: files){
                if(file.exists()) {//如果文件存在
                    //file.delete();// 则先删除
                    FileWriter fw = new FileWriter(file);
                    fw.write("");
                    fw.close();
                }
                file.createNewFile();	//再创建
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTime = System.currentTimeMillis();    //获取开始时间
        SpiderController.getAllLinks(path);	//开始爬取目标内容
//        System.out.println(""
//                + "\n图片总数："+getFileCount(imgLinkFile)+"张"
//                );

        //writeTxtFile(imgLinkFile, "图片总数："+getFileCount(imgLinkFile)+"张");

        long endTime = System.currentTimeMillis();    //获取结束时间
//        System.out.println("\n程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        List<String> list = getFileContext(filePath + "ImgLinks.txt");
        ArrayList<String> newlist = new ArrayList<> ();





        for(int i = 0 ; i < list.size() ; i++) {
            String string = list.get(i);
            //  System.out.println(string.substring(34, 37));
            if(string.substring(34, 38).equals("item")){

                newlist.add(string);
            }

        }


//        for(int i = 0 ; i < newlist.size() ; i++){
//            System.out.println(newlist.get(i));
//        }


        return newlist;

    }






}
