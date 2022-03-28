package com.atguigu.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.util.Collector;

// batch process Word Count
public class WordCount {
    public static void main(String[] args) throws Exception{
        // create excute enviroment
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 从文件中读取数据
        String inputPath = "D:\\flink_project\\src\\main\\resources\\hello.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);
        // 空格分词打散之后，对单词进行groupby分组，然后用sum进行聚合
        DataSet<Tuple2<String, Integer>> wordCountDataSet = inputDataSet.flatMap(new MyFlatMapper()).groupBy(0) .sum(1);
        // 打印输出
        wordCountDataSet.print();
    }


    // 自定义类，实现FlatMapFunction接口
    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {
        //@Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            // 按空格分词
            String[] words = value.split(" ");
            // 遍历所有word，包成二元组输出
            for (String word : words) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }
}
