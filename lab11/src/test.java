import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
public class test {
    private static JLabel label;
    private static String fileName = null;
    private static String resultsFileName = null;
    private static String graphFileName = null;
    private static String pathGraphFileName = null;
    private static String randomWalkFileName = null;
    private static ImageIcon img;
    private static translatorFrame frame ;
    //main function gui的运行界面
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                frame = new translatorFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
    //show frame 顶级容器，作为主窗口的函数
    static class translatorFrame extends JFrame{
        public translatorFrame(){

            setTitle("文本有向图");
            //设定gui窗口的高度宽度
            setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
            controlPanel control = new controlPanel();
            add(control,BorderLayout.SOUTH);
            label = new JLabel("");
            label.setLayout(new FlowLayout());
            add(label);
        }
        private static final int DEFAULT_WIDTH = 1000;
        private static final int DEFAULT_HEIGHT = 800;
    }
    //contro panel 轻量级容器 用于添加按钮，下面所有的函数都是为了actionPerformed的动作实现
    static class controlPanel extends JPanel{
        //all the buttons 所有的按钮，包括了实验的所有要求
        public controlPanel(){
            //setLayout(new BorderLayout());

            panel = new JPanel();
            //panel.setLayout(new GridLayout(3,2));

            //fun1:open file
            addButton("open file",new FileAction());

            //fun2:show
            addButton("show",new ShowAction());

            //fun3:bridge word
            addButton("bridge word",new WordBridgeAction());

            //fun4:new text
            addButton("new text",new NewTxtAction());

            //fun5:shortest path
            addButton("shortest path",new ShortestPathAction());

            //fun6:cal pagerank
            addButton("cal PageRank", new PageRankAction());

            //fun7:random walk
            addButton("random walk",new RandomWalkAction());

            //addButton("clear",new ClearAction());
            add(panel,BorderLayout.CENTER);
        }
        //添加按钮，listener作为监听对象，也就是实现接口，实现用户点击后的操作
        private void addButton(String label,ActionListener listener){
            JButton button = new JButton(label);
            button.addActionListener(listener);
            panel.add(button);
        }
        private class FileAction implements ActionListener{
            //对字符串的处理，大写变小写，删去标点符号
            public void readFileToWords(){
                File file = new File(fileName);
                BufferedReader reader = null;
                try{
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    while((tempString = reader.readLine())!=null){
                        String s = tempString.replaceAll("[\\p{Punct}]+", " ");  //标点变成空格
                        System.out.println(s);
                        String[] Words = s.trim().split("\\s+");   //按空格分割

                        for(int i = 0;i<Words.length;i++)   //正则表达式匹配字母并变成小写
                        {
                            Pattern p = Pattern.compile("a-z||A-Z");
                            Matcher m = p.matcher(Words[i]);
                            Words[i] = m.replaceAll("").trim().toLowerCase();
                        }
                        //下面的循环，将处理好的字符串，写到Results.txt中
                        for(String str :Words){
                            write(fileName.substring(0, fileName.length()-4) +"Results.txt",str+"\r\n");
                        }
                    }
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch(IOException e1){
                        }
                    }
                }
            }
            //将读入的单词，转换成有向图
            public void readWordsToGraph(){
                graph = new DGraph();
                File file = new File(resultsFileName);
                BufferedReader reader = null;
                try{
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    String word1 = null;
                    String word2 = null;
                    //下面的循环意思就是，读到文件末尾，为每相邻的两个单词创建有向图的边
                    while((tempString = reader.readLine())!=null){
                        word2 = tempString;
                        if(word1 != null){
                            //是自己实现的addedge函数
                            graph.addEdge(word1, word2);
                        }
                        word1 = tempString;
                        System.out.println(tempString);
                    }
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch(IOException e1){
                        }
                    }
                }
            }
            //处理动作事件的代码，也就是点击按钮之后，会用这个函数来处理
            public void actionPerformed(ActionEvent e){
                //file chooser swing GUI中自带的文件选择
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isDirectory()){
                    System.out.println("文件夹:"+file.getAbsolutePath());
                }else if(file.isFile()){
                    System.out.println("文件:"+file.getAbsolutePath());
                }
                System.out.println(jfc.getSelectedFile().getName());
                //read file 初始化好输出文件的名字
                fileName = file.getAbsolutePath();
                resultsFileName = fileName.substring(0, fileName.length()-4) +"Results.txt";
                randomWalkFileName = resultsFileName.substring(0, resultsFileName.length()-4) +"randomWalk"+ "." + "txt";
                //System.out.println("pathGraphFileName: "+pathGraphFileName);
                readFileToWords();
                readWordsToGraph();
                //graphFileName = graph.showGraph(resultsFileName);
            }
        }
        private class ShowAction implements ActionListener{
            public void actionPerformed(ActionEvent e){
                //graph.showGraph(resultsFileName);
                showDirectedGraph(graph);
            }
        }
        private class WordBridgeAction implements ActionListener{
            public void actionPerformed(ActionEvent e){

                String word1=null,word2=null;
                word1 = JOptionPane.showInputDialog("word1:");
                word2 = JOptionPane.showInputDialog("word2:");
                //调用了graph对象中的这个函数
                String wordBridge = graph.queryBridgeWords(word1, word2);
                JOptionPane.showMessageDialog(null, wordBridge,"bridge word", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(word1+word2);
            }
        }
        private class NewTxtAction implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String sentence = JOptionPane.showInputDialog("sentence:");
                //调用了graph对象中的这个函数
                String newText = graph.generateNewText(sentence);
                JOptionPane.showMessageDialog(null, newText,"new text", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        private class ShortestPathAction implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String resultsShortestPath = null;
                String[]  resultsShortestPathes;
                String word1=null,word2=null;
                //JOptionPane.showInputDialog("word2:", JOptionPane.CANCEL_OPTION);
                word1 = JOptionPane.showInputDialog("word1:");
                word2 = JOptionPane.showInputDialog("word2:");
                System.out.println("word1: "+word1+" ,word2: "+word2);
                if(word2 == null){
                    //必须取消输入word2才能进入
                    resultsShortestPathes = graph.calcShortestPath(word1);
                    for(String path:resultsShortestPathes){
                        int res = JOptionPane.showConfirmDialog(null,"next are the shortest path begin with word "+word1,"continue or not",JOptionPane.YES_NO_OPTION);
                        if(res == JOptionPane.NO_OPTION){
                            break;
                        }
                        else{
                            String srr[] = path.split("@");
                            showPathes(graph,srr);
                            String p[] = srr[0].split(" ");
                            word2 = p[p.length-1];
                            JOptionPane.showMessageDialog(null, "From \""+word1+"\" to \""+word2+"\" there are "+srr.length+" shortest path:   \r\n"+path,"shortest path", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    //需要判断一下这两个词是不是空的
                }else if(word1 != null &&word2!= null){
                    resultsShortestPath = graph.calcShortestPath(word1, word2);
                            // 明确检查不可达的情况
                    if (resultsShortestPath.equals("不可达！")) {
                        JOptionPane.showMessageDialog(
                        null, 
                        "不可达！", 
                        "shortest path", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    } 
                    else{
                        String srr[] = resultsShortestPath.split("@");
                        showPathes(graph,srr);
                        JOptionPane.showMessageDialog(null, "From \""+word1+"\" to \""+word2+"\" there are "+srr.length+" shortest path:   \r\n"+resultsShortestPath,"shortest path", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        }
        private class PageRankAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                graph.calPageRank(100, 0.85); // 迭代100次，阻尼因子0.85
                
                String word = JOptionPane.showInputDialog("Enter a word:");
                if (word != null && !word.trim().isEmpty()) {
                    Double pr = graph.getPageRank(word.toLowerCase());
                    if (pr != null) {
                        JOptionPane.showMessageDialog(null, 
                            String.format("PageRank of '%s' is: %.4f", word, pr),
                            "PageRank Result", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Word not found!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        
        //随机游走
        private class RandomWalkAction implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String reslutsRandomWalk = graph.randomWalk();
                showPath(graph,reslutsRandomWalk);
                JOptionPane.showMessageDialog(null, "\""+reslutsRandomWalk+"\""+" has been stored in "+randomWalkFileName,"random walk",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        private JPanel panel;
        private DGraph graph ;
        private static final int LENGTH_WORD = 10;
        private static final int LENGTH_SENT = 30;
    }
    //show directed graph
    static void showDirectedGraph(DGraph graph) {
        graphFileName = graph.showGraph(resultsFileName);
        try {
            // 读取原始图片
            BufferedImage originalImage = ImageIO.read(new File(graphFileName));
            if (originalImage == null) {
                label.setText("图片加载失败: 文件格式不支持或文件损坏");
                return;
            }
            
            int maxWidth = 1000; // 最大宽度与窗口匹配
            int maxHeight = 700;
    
            // 直接使用 BufferedImage 的 getWidth() 和 getHeight()（不需要参数）
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
    
            // 计算缩放比例
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);
            
            // 生成缩放后的图片
            int scaledWidth = (int) (originalWidth * ratio);
            int scaledHeight = (int) (originalHeight * ratio);
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            
            // 更新标签显示
            label.setIcon(new ImageIcon(scaledImage));
            label.setText(null);
        } catch (IOException e) {
            label.setText("图片加载失败: " + e.getMessage());
        }
    }
    static void showPath(DGraph graph, String Path) {
        pathGraphFileName = graph.showGraphPath(resultsFileName, Path);
        System.out.println("pathGraphFileName:" + pathGraphFileName);
        
        try {
            BufferedImage originalImage = ImageIO.read(new File(pathGraphFileName));
            if (originalImage == null) {
                label.setText("图片加载失败: 文件格式不支持或文件损坏");
                return;
            }
    
            int maxWidth = 1000; // 与窗口宽度匹配
            int maxHeight = 700;
    
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
    
            double ratio = Math.min((double) maxWidth / originalWidth, 
                                  (double) maxHeight / originalHeight);
            int scaledWidth = (int) (originalWidth * ratio);
            int scaledHeight = (int) (originalHeight * ratio);
    
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
            label.setText(null);
        } catch (IOException e) {
            label.setText("图片加载失败: " + e.getMessage());
        }
    }
    
    static void showPathes(DGraph graph, String[] Pathes) {
        pathGraphFileName = graph.showGraphPathes(resultsFileName, Pathes);
        System.out.println("pathGraphFileName:" + pathGraphFileName);
        
        try {
            BufferedImage originalImage = ImageIO.read(new File(pathGraphFileName));
            if (originalImage == null) {
                label.setText("图片加载失败: 文件格式不支持或文件损坏");
                return;
            }
    
            int maxWidth = 1000;
            int maxHeight = 700;
    
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
    
            double ratio = Math.min((double) maxWidth / originalWidth, 
                                  (double) maxHeight / originalHeight);
            int scaledWidth = (int) (originalWidth * ratio);
            int scaledHeight = (int) (originalHeight * ratio);
    
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
            label.setText(null);
        } catch (IOException e) {
            label.setText("图片加载失败: " + e.getMessage());
        }
    }
    //先把源txt文件进行加工整理
    static void write(String fileName,String str){
        try{
            //new File(fileName);
            FileWriter writer = new FileWriter(fileName,true);
            writer.write(str);;
            System.out.println("写入文件成功");
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}