# AndroidHosts

![image](https://github.com/Joshua-Zheng/HostsUpdate/raw/master/Screenshot.png)

This is an Android Application of get the hosts file.

The Application will change a file which is in "/system/etc/hosts",
So, before you use this Application, You'd better Root your device first,
and then you will get a message to tell you to choose "Allow" or "Refuse"

You'd better choose "Allow", in order to make the Application running normally.

#For Developers
The code is updated to use Java 8, you should add some following code into "app-build.gradle"  

android {  

    defaultConfig {  
    
        //Use Jack Translater  
        jackOptions{  
        
            enabled true  
        }  
    }  
      
    //Use Java 1.8  
    
    compileOptions {  
    
        sourceCompatibility JavaVersion.VERSION_1_8  
        
        targetCompatibility JavaVersion.VERSION_1_8  
    }  
    
}

#Hosts更新

这是一个获取hosts文件的Android应用，它将更改/system/etc/hosts文件，
所以在使用此程序前，你最好先获得设备的Root权限，然后选择 "允许" 操作，以保证应用的正常运行。
