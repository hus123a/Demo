package com.catCoder.baisc;

import java.util.Arrays;

/**
 * 快速排序
 */
public class QuickSort {
    //快速排序
    public static void main(String[] args) {
        int[] arr = {4,8,0,2,45,6,3,7,78,26,3,23,67,51,57,23,78,135,38};
        System.out.println(Arrays.toString(arr));
        quick(arr, 0 , arr.length-1);

        System.out.println(Arrays.toString(arr));
    }



    public static void quick(int[] arr, int low, int high){
        System.out.println("low:"+low+", high:" + high);
        if(low >= high) {
            return;
        }
        int lowIndex = low;
        int highIndex = high;
        int provit = arr[low];
        int temp = arr[lowIndex];
        //双向交替遍历
        while (lowIndex < highIndex) {

            //先从右开始往左遍历
            while(highIndex > lowIndex && arr[highIndex] >= provit){
                highIndex -- ;
            }
            //从左往右遍历

            while(lowIndex < highIndex && arr[lowIndex] <= provit){
                lowIndex ++;
            }

            //交换
            if(lowIndex < highIndex){
                swap(arr, lowIndex, highIndex);
            }

        }

        //重置分割点

        arr[low] = arr[lowIndex];
        arr[lowIndex] = provit;

        System.out.println("lowIndex:"+lowIndex+", highIndex:" + highIndex);
        System.out.println(Arrays.toString(arr));

        quick(arr, low, lowIndex-1);

        quick(arr, highIndex+1, high);

    }

    public static void swap(int[] arr, int i, int j){
        int temp;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

    }

    public static void topK(int[] arr, int low, int high, int k){

        if(low >= high) {
            return;
        }
        int lowIndex = low;
        int highIndex = high;
        int provit = arr[low];
        int temp = arr[lowIndex];
        //双向交替遍历
        while (lowIndex < highIndex) {
            //先从右开始往左遍历
            while(highIndex > lowIndex && arr[highIndex] >= provit){
                highIndex -- ;
            }
            //从左往右遍历
            while(lowIndex < highIndex && arr[lowIndex] <= provit){
                lowIndex ++;
            }
            //交换
            if(lowIndex < highIndex){
                swap(arr, lowIndex, highIndex);
            }
        }
        //重置分割点
        arr[low] = arr[lowIndex];
        arr[lowIndex] = provit;
        if(provit > k){
            if(high - highIndex > k){

            }
        }

        quick(arr, low, lowIndex-1);
        quick(arr, highIndex+1, high);
    }
}
