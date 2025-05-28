import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class Whitetest {
    public void readWordsToGraph(DGraph graph, String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            String word1 = null;
            String word2 = null;
            while ((tempString = reader.readLine()) != null) {
                word2 = tempString;
                if (word1 != null) {
                    graph.addEdge(word1, word2);
                }
                word1 = tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    @Test
    public void test0() {
        // 空文件
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/empty.txt");
            String result = graph.queryBridgeWords("word1", "word2");
            System.out.println(result);
            assertEquals("No \"word1\" or \"word2\" in the graph!", result);
        });
    }

    @Test
    public void test1() {
        // 有多个桥接词
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("to", "out");
            System.out.println(result);
            assertEquals("The bridge words from \"to\" to \"out\" are:seek,put,", result);
        });
    }

    @Test
    public void test2() {
        // 不存在单词（word1不存在）
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("123", "to");
            System.out.println(result);
            assertEquals("No \"123\" or \"to\" in the graph!", result);
        });
    }

    @Test
    public void test3() {
        // 不存在单词（word2不存在）
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("to", "456");
            System.out.println(result);
            assertEquals("No \"to\" or \"456\" in the graph!", result);
        });
    }

    @Test
    public void test4() {
        // 不存在单词（word1和word2都不存在）
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("123", "456");
            System.out.println(result);
            assertEquals("No \"123\" or \"456\" in the graph!", result);
        });
    }

    @Test
    public void test5() {
        // 无桥接词
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("to", "new");
            System.out.println(result);
            assertEquals("No bridge words from \"to\" to \"new\"!", result);
        });
    }

    @Test
    public void test6() {
        // word1和word2相同且存在桥接词
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("to", "to");
            System.out.println(result);
            assertEquals("The bridge words from \"to\" to \"to\" are:do,", result);
        });
    }

    @Test
    public void test7() {
        // 有1个桥接词
        assertTimeout(Duration.ofSeconds(1), () -> {
            DGraph graph = new DGraph();
            readWordsToGraph(graph, "src/exampleResults.txt");
            String result = graph.queryBridgeWords("to", "strange");
            System.out.println(result);
            assertEquals("The bridge words from \"to\" to \"strange\" are:explore,", result);
        });
    }
}