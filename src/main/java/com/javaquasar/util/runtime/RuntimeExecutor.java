package com.javaquasar.util.runtime;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Java Quasar
 */
public class RuntimeExecutor {

    public static void main(String[] args) throws IOException {
                String json =                             "--json={\"menu\": {\n" + 
                                                  "  \"id\": \"file\",\n" + 
                                                  "  \"value\": \"File\",\n" + 
                                                  "  \"popup\": {\n" + 
                                                  "    \"menuitem\": [\n" + 
                                                  "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" + 
                                                  "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" + 
                                                  "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" + 
                                                  "    ]\n" + 
                                                  "  }\n" + 
                                                  "}}";
        //json = json.replaceAll("\\s", "");
        //System.out.println(json);
        
        Process proc = Runtime.getRuntime().exec("java -jar /home/artur/NetBeansProjects/DiPocket/EnterprieSolutions/InControl/dist/InControl.jar --session_name=sesion_380505217132 --pan=1234123412341234 --operation=reg --expiry=1818 --debug=true " + json);
        Runtime.getRuntime().traceMethodCalls(true);
        Runtime.getRuntime().traceInstructions(true);
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
        /*try {
            proc.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        String sInfo = convertStreamToTring(in);
        System.out.print(sInfo);

        String sError = convertStreamToTring(err);
        System.out.println(sError);
        //System.out.println(Runtime.getRuntime().freeMemory());

    }

    public static String convertStreamToTring(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }

        /*java.io.InputStream is = proc.getInputStream();
        byte b[] = new byte[is.available()];
        is.read(b, 0, b.length);
        System.out.println(new String(b));*/
    }

    private void logForShell(Process p, String headStdOut, String headStdErr) throws IOException {
        String s = null;
        StringBuilder std = new StringBuilder();
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            //stdOut
            while ((s = stdInput.readLine()) != null) {
                std.append(s + "\n");
            }
            if (!std.toString().isEmpty()) {
                //LoggerFactory.getLogger().log(headStdOut + std.toString());
            }
            std = new StringBuilder();
            //stdErr
            while ((s = stdError.readLine()) != null) {
                std.append(s + "\n");
            }
            if (!std.toString().isEmpty()) {
                //LoggerFactory.getLogger().log(headStdErr + std.toString());
                throw new RuntimeException();
            }
        }
    }

    public static void excuteCommand(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new IllegalArgumentException("The file " + filePath + " does not exist");
        }
        if (RuntimeExecutor.isLinux()) {
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", filePath}, null);
        } else if (RuntimeExecutor.isWindows()) {
            Runtime.getRuntime().exec("cmd /c start " + filePath);
        }
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("windows") >= 0;
    }

}
