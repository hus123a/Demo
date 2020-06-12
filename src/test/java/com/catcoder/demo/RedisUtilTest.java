package com.catcoder.demo;


import com.catcoder.demo.bean.MyLinkTreeNode;
import com.catcoder.demo.service.AddNewTreeThread;
import com.catcoder.demo.service.ITreeService;
import com.catcoder.demo.service.RollBack;
import com.catcoder.demo.utils.RedisLock;
import com.catcoder.demo.utils.RedisUtil;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  {DemoApplication.class})
public class RedisUtilTest {
    private final Logger logger = LoggerFactory.getLogger(RedisUtilTest.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITreeService treeService;






    @Test
    public void testSet(){
        try{
            redisUtil.set("hello","world");
            System.out.println("设置成功！");
        }catch (Exception e){
            logger.error("设置失败！", e);
        }
    }

    @Test
    public void testSet2(){
        try{
//            redisUtil.set("hello","world");
            redisTemplate.opsForValue().set("hello","world");
            System.out.println("设置成功！");
        }catch (Exception e){
            logger.error("设置失败！", e);
        }
    }

    @Test
    public void test3(){
        List<MyLinkTreeNode> test = Lists.newArrayList();

        for (int i = 1; i< 11; i++) {
            MyLinkTreeNode temp = new MyLinkTreeNode();
            temp.setName("node"+i);
            temp.setPid(i);
            temp.setValue("节点"+i);
            temp.setLevel(i);
            test.add(temp);
        }

        addNewTrees(test);




        //线程池测试
        //ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>());
    }
    @Test
    public void test4(){
        /*MyLinkTreeNode temp = new MyLinkTreeNode();
        temp.setName("nodetest");
        temp.setPid(100001);
        temp.setValue("节点"+100001);
        temp.setLevel(1);
        System.out.println(treeService.addTree(temp));*/
        HashMap<Integer, Character> map = new HashMap<Integer, Character>();


        /**
         * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
         * 使用 int 算法，这里 s[i] 是字符串的第 i 个字符，n 是字符串的长度，^ 表示求幂。空字符串的哈希值为 0。
         */

        String key = "hello";

        char[] chars = key.toCharArray();

//        for (char temp : chars) {
//            map.put(key.indexOf(temp), temp);
//
//        }
        for(int i = 0; i < chars.length; i++) {
            map.put(i, chars[i]);
        }


        int hash = 0;
        int hash2 = 0;
        for (Map.Entry<Integer, Character> entry: map.entrySet()) {

            System.out.println(entry.getKey() + "  " + entry.getValue() + " 幂的次数 ：" + (map.size() - entry.getKey() - 1));

            hash += entry.getValue() == ' ' ? 0 : (int) (entry.getValue() * Math.pow(31, map.size() - entry.getKey() - 1));
        }

        hash2 = (int) ('h'*Math.pow(31,4) + 'e'*Math.pow(31,3) +'l'*Math.pow(31,2) + 'l'*Math.pow(31,1) +'o'*Math.pow(31,0));

        System.out.println(key + "的hash值是："+ hash);
        System.out.println(key + "的hash值是："+ hash2);
        System.out.println(key + "的hash值是："+ key.hashCode());




    }
    @Test
    public void test5() throws Exception {
//        int[] a = {2,-1,4,32,5,-9};
//
//        System.out.println(subarraysDivByK(a, 0));
        //String s = "3[a]2[bc]"; //返回 "aaabcbc".
        //String s = "3[a2[c]]", 返回 "accaccacc".
        //String s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".
        //decodeString(s);

//        int[] nums = {3,2,4};
//        int target = 6;
//
//        printArray(twoSum1(nums, target));
        //int x = 11001001;  //10010011  10010011
//        int x = 2147483647; //-321
//
//
//        System.out.println(reverse(x));
        String s1 = "abc";
        String s2 = "bad";
        System.out.println(CheckPermutation(s1, s2));


    }

    public boolean CheckPermutation(String s1, String s2) {
        if(s1.length() != s2.length() ){
            return false;
        }

        int sum1 =0;
        int sum2 = 0;

        for(int i = 0; i< s1.length();i++  ){
            sum1 += s1.charAt(i);
            sum2 += s2.charAt(i);
        }
        System.out.println("sum1:"+sum1 +" sum2:"+ sum2);
        return sum1 == sum2;
    }

    public int lengthOfLongestSubstring(String s) {
        int ans = 0;
        StringBuffer stringBuffer = new StringBuffer();
        int max = 0;
        char[] chars = s.toCharArray();
        for (int i = 0 ; i < chars.length ; i++) {
            if (stringBuffer.toString().contains(chars[i]+"")){
                stringBuffer = new StringBuffer(chars[i-1]);
                ans = ans > max? ans: max;
                max = 1;
            }
            stringBuffer.append(chars[i]);
            max++;
        }
        return ans > max? ans: max;
    }


    public int subarraysDivByK(int[] A, int K) {
        int num = 0;
        //排序
        Arrays.sort(A);
        if(A[0]>5){
            return num;
        }
        printArray(A);
        //换算
        ArrayList<int[]> arrayList = new ArrayList<>();
        int[] temp = {};
        int m = A.length;
        for (int j = 0 ; j <=m; j++) {
            for(int l = 0; l <=m; l++){
                temp = Arrays.copyOfRange(A,j,l);
                arrayList.add(temp);
                System.out.println("从"+j+"到"+ l);
                printArray(temp);
            }
        }

        num = arrayList.size();

        return num;
    }

    private void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            if(i == arr.length - 1) {
                System.out.print(arr[i]);
            }else{
                System.out.print(arr[i]+",");
            }
        }
        System.out.println("]");
    }

    public String decodeString(String s) {

        int  f = 0;
        int  e = 0;
        /*int i = s.indexOf("[");
        if((f = s.indexOf("[") )!= -1){
            e = s.lastIndexOf("]");
            System.out.println(s.charAt(f-1)+" "+ s.substring(f,e));

        }
        */

        char[] chars = s.toCharArray();

        /*for (int i = 0; i < chars.length; i++) {
            if( 0 <= chars[i] && chars[i] <= 9) {

            }
        }*/


        return "";
    }

    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        for (int i = 0; i < nums.length; i++) {
           for (int j = i+1; j< nums.length; j++) {
                if(nums[i] + nums[j] == target){
                    ans[0] = i;
                    ans[1] = j;
                    return ans;
                }
           }
        }
        return ans;
    }
    public int[] twoSum1(int[] nums, int target) throws Exception {
        int[] ans = new int[2];
        HashMap<Integer, Integer> numMaps = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if(numMaps.containsKey(target - nums[i])) {
                ans[1] = i;
                ans[0] = numMaps.get(target - nums[i]);
                return ans;
            }
            numMaps.put(nums[i], i);
        }
        return ans;
    }

    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> booleanList = new ArrayList<>(candies.length);
        //获取最大值
        int max = 0;
        for (int candy : candies) {
            max = candy > max ? candy : max;
        }
        for (int candy : candies) {
            booleanList.add(candy + extraCandies >= max);
        }
        return  booleanList;
    }


    public int reverse(int x) {
        if(x == 0 || x == -2147483648) {
            return 0;
        }
        String tag = x<0? "-":"";
        if(x<0){
            x = Integer.parseInt((x+"").substring(1));
        }
        LinkedList<Integer> numLink = new LinkedList<>();
        while (x >0) {
            numLink.addFirst(x % 10);
            x = x/10;
        }
        StringBuffer node = new StringBuffer(tag);
        int num = 0;
        while (numLink.size()>0){
            num = numLink.removeLast();
            if(!(num ==0 && node.length() == 0)) {
                node.append(num);
            }
        }
        System.out.println(node);
        return Double.parseDouble(node.toString())<Integer.MIN_VALUE
                || Double.parseDouble(node.toString())>Integer.MAX_VALUE ?
                 0 : Integer.parseInt(node.toString());
    }

    public boolean isPalindrome(int x) {
        boolean ans = false;
        int temp = 0;
        int sum = 0;
        while(x<0) {
            temp = x % 10;
        }
        return ans;
    }

    public int sumNums(int n) {
        boolean flag = n > 0 && (n += sumNums(n - 1)) > 0;
        return n;
    }

    class ListNode {
       int val;
       ListNode next;
       ListNode(int x) { val = x; }
   }

    class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode ans = new ListNode(0);
            int x = 0;
            int valuel1 = 0;
            int valuel2 = 0;
            ListNode temp = null;
            while(l1 != null || l2 != null){
                ListNode e1 = l1;
                ListNode e2 = l2;

                valuel1 = l1 == null ? 0 : e1.val;
                valuel2 = l2 == null ? 0 : e2.val;

                if( temp == null) {
                    temp = ans;
                }
                ListNode listNode = new ListNode((valuel1 + valuel2 + x) % 10);
                temp.next = listNode;
                temp = listNode;

                x = (valuel1 + valuel2 + x)/10;
                l1 = l1 == null? null : l1.next;
                l2 = l2 == null? null : l2.next;
            }

            if(x>0) {
                temp.next = new ListNode(x);
            }
            return ans.next;
        }
    }




    //@Test
    public void addNewTrees(List<MyLinkTreeNode> treeList) throws SystemException {
        logger.info("addNewCompanyUsers 新增参保人方法");
        logger.info(">>>>>>>>>>>>参数:{}", treeList);
        if (CollectionUtils.isEmpty(treeList)) {
            logger.info(">>>>>>入参为空。");
            return;
        }

        //每条线程最小处理任务数
        int perThreadHandleCount = 1;
        //线程池的最大线程数
        int nThreads = 10;
        int taskSize = treeList.size();

        if (taskSize > nThreads * perThreadHandleCount) {
            perThreadHandleCount = taskSize % nThreads == 0 ? taskSize / nThreads : taskSize / nThreads + 1;
            nThreads = taskSize % perThreadHandleCount == 0 ? taskSize / perThreadHandleCount : taskSize / perThreadHandleCount + 1;
        } else {
            nThreads = taskSize;
        }

        logger.info("批量添加树taskSize: {}, perThreadHandleCount: {}, nThreads: {}", taskSize, perThreadHandleCount, nThreads);
        CountDownLatch mainLatch = new CountDownLatch(1);
        //监控子线程
        CountDownLatch threadLatch = new CountDownLatch(nThreads);
        //根据子线程执行结果判断是否需要回滚
        BlockingDeque<Boolean> resultList = new LinkedBlockingDeque<>(nThreads);
        //必须要使用对象，如果使用变量会造成线程之间不可共享变量值
        RollBack rollBack = new RollBack(false);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);

        List<Future<List<Object>>> futures = Lists.newArrayList();
        List<Object> returnDataList = Lists.newArrayList();
        //给每个线程分配任务
        for (int i = 0; i < nThreads; i++) {
            int lastIndex = (i + 1) * perThreadHandleCount;
            List<MyLinkTreeNode> companyUserResultVos = treeList.subList(i * perThreadHandleCount, lastIndex >= taskSize ? taskSize : lastIndex);
            AddNewTreeThread addNewCompanyUserThread = new AddNewTreeThread(mainLatch, threadLatch, rollBack, resultList, companyUserResultVos, treeService);
            Future<List<Object>> future = fixedThreadPool.submit(addNewCompanyUserThread);
            futures.add(future);
        }

        /** 存放子线程返回结果. */
        List<Boolean> backUpResult = Lists.newArrayList();
        try {
            //等待所有子线程执行完毕
            boolean await = threadLatch.await(20, TimeUnit.SECONDS);
            //如果超时，直接回滚
            if (!await) {
                rollBack.setRollBack(true);
            } else {
                logger.info("创建参保人子线程执行完毕，共 {} 个线程", nThreads);
                //查看执行情况，如果有存在需要回滚的线程，则全部回滚
                for (int i = 0; i < nThreads; i++) {
                    Boolean result = resultList.take();
                    backUpResult.add(result);
                    logger.debug("子线程返回结果result: {}", result);
                    if (result) {
                        /** 有线程执行异常，需要回滚子线程. */
                        rollBack.setRollBack(true);
                    }
                }
            }
        } catch (InterruptedException e) {
            logger.error("等待所有子线程执行完毕时，出现异常");
            throw new RuntimeException("等待所有子线程执行完毕时，出现异常，整体回滚");
        } finally {
            //子线程再次开始执行
            mainLatch.countDown();
            logger.info("关闭线程池，释放资源");
            fixedThreadPool.shutdown();
        }

        /** 检查子线程是否有异常，有异常整体回滚. */
        for (int i = 0; i < nThreads; i++) {
            if (!CollectionUtils.isEmpty(backUpResult)) {
                Boolean result = backUpResult.get(i);
                if (result) {
                    logger.info("创建参保人失败，整体回滚");
                    throw new RuntimeException("创建参保人失败");
                }
            } else {
                logger.info("创建参保人失败，整体回滚");
                throw new RuntimeException("创建参保人失败");
            }
        }

        //拼接结果
        try {
            for (Future<List<Object>> future : futures) {
                returnDataList.addAll(future.get());
            }
        } catch (Exception e) {
            logger.info("获取子线程操作结果出现异常,创建的参保人列表： {} ，异常信息： {}", e);
            throw new RuntimeException("创建参保人子线程正常创建参保人成功，主线程出现异常，回滚失败");
        }
        logger.info("全部插入成功");
    }



}