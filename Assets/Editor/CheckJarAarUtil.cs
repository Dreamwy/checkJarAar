using UnityEditor;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Reflection;
using System.IO;

namespace CMPMopubIntegratedSDK.Editor
{
    public class CheckJarAarUtil
    {
    [MenuItem ("Util/checkJAR&AAR")]
	public static void checkJARandAAR(){
		// string[] adb = {"abc1.jar","abc2.jar","abc3.jar"};
		GetConflictMessages(findAARandJar());
	}

	 public static void GetConflictMessages(List<String> jars)
        {
            if (jars == null || jars.Count < 2)
                return;

            var jarArguments = "";
            foreach (var jar in jars)
                jarArguments += " \"" + jar + "\"";

			Debug.Log(jarArguments);
            var processStartInfo = new System.Diagnostics.ProcessStartInfo();

            processStartInfo.FileName = "java";
            processStartInfo.Arguments = "-jar \"" + Application.dataPath + "/Plugins/Android/checkjaraar.jar\"" + jarArguments;
            processStartInfo.RedirectStandardOutput = true;
            processStartInfo.RedirectStandardError = true;
            processStartInfo.UseShellExecute = false;

            var process = System.Diagnostics.Process.Start(processStartInfo);
			string str = System.Diagnostics.Process.GetCurrentProcess().MainModule.FileName;
            process.WaitForExit();
            var standardOutput = process.StandardOutput.ReadToEnd();
            if (standardOutput != "")
                Debug.Log(standardOutput);

            var standardError = process.StandardError.ReadToEnd();
            if (standardError != "")
                Debug.LogError(standardError);
        }

		public static List<String> findAARandJar(){
				//路径  
			string fullPath = "Assets/";  //路径
			List<String> names = new List<String>();
			//获取指定路径下面的所有资源文件  
			if (Directory.Exists(fullPath)){  
				DirectoryInfo direction = new DirectoryInfo(fullPath);  
				FileInfo[] files = direction.GetFiles("*",SearchOption.AllDirectories);  
				for(int i=0;i<files.Length;i++){ 
					if (files[i].Name.EndsWith(".aar")||files[i].Name.EndsWith(".jar")){  
						names.Add(files[i].DirectoryName +"/" + files[i].Name);
					} 
				}  
			} 
			return names;
		}
    }
}