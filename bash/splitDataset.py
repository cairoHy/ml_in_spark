#!/usr/bin/python3.3
# coding: UTF-8

import os,sys,shutil
import random
from subprocess import call

def chooseFile():
    path = sys.argv[1]
    fileList = os.listdir(path)
    fileNum = (int)(len(fileList) / 10)
    fileNum = [1,fileNum][fileNum >= 1]
    chosenFile = random.sample(fileList,fileNum)
    print(chosenFile)
    return chosenFile

def splitDatasetToDirectory(chosenFile):
    """将NetFlix数据集目录下的小文件随机选择10%复制到另外一个文件夹"""
    path = sys.argv[1]
    for file in chosenFile:
        if sys.platform.__eq__("win32"):
            desPath = "c:/Users/zhy/Documents/study/AD.SE/courseDesign/DatasetInHDFS/NetFlix/little/"
            shutil.copy(path + file, desPath)
        else:
            call(["cp", path + file, path + "../little/"])

def splitDatasetToHDFS(chosenFile):
    """将NetFlix数据集目录下的小文件随机选择10%上传至HDFS中以测试使用"""
    path = sys.argv[1]
    for file in chosenFile:
        call(["hdfs","dfs","-put",path + file,"/zhy/data/NetFlix/little/"])

def inputParm():
    if len(sys.argv) < 2:
        print("命令格式：./splitDataset.py [YourDataSetPath]")
        sys.exit(1)
    trigger = True
    while trigger:
        try:
            trigger = False
            print("1 -> 抽取 “" + sys.argv[1] + 
                "” 目录下10%的文件并复制到“../little/”目录下\n")
            print("2 -> 抽取 “" + sys.argv[1] + 
                "” 目录下10%的文件并上传到HDFS中“zhy/data/NetFlix/little/”目录下\n")
            param = int(input('请输入选择的操作 ... \n'))
            return param

        except ValueError:
            trigger = True
            print("输入不合法，请输入一个数字 ... ")

if __name__ == '__main__':
    param = inputParm()
    if param == 1:
        splitDatasetToDirectory(chooseFile())
    elif param == 2:
        splitDatasetToHDFS(chooseFile())
    else:
        print("未知选项，退出程序")
        sys.exit(1)
