import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, TemplateException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter project name. e.g Sample-Project");
        String projectName = scanner.next();
        System.out.println("Enter environments with comma separated values. e.g local,aat,sit");
        String[] environments = scanner.next().split(",");
        System.out.println("Enter group id. e.g org.test.sample-project");
        String groupId = scanner.next();
        System.out.println("Do you want to create ESB project. Y/N");
        String createESBProject = scanner.next();
        System.out.println("Do you want to create Registry project. Y/N");
        String createRegProject = scanner.next();
        String[] customDirs = null;
        if ("y".equalsIgnoreCase(createRegProject)) {
            System.out.println("Do you want to create custom directories. Y/N");
            if ("y".equalsIgnoreCase(scanner.next())) {
                System.out.println("Enter custom directory paths if needed. e.g xslt, xsd, endpoints/local, endpoints/sit");
                customDirs = scanner.next().split(",");
            }
        }
        System.out.println("Do you want to create DSS project. Y/N");
        String createDSSProject = scanner.next();


        String targetProjectName = "";
        List<String> modules = new ArrayList<String>();

        //Create REG project
        if ("y".equalsIgnoreCase(createRegProject)) {
            targetProjectName = projectName + "REG";
            modules.add(targetProjectName);
            System.out.println("Generating project :" + targetProjectName);
            RegistryProject registryProject = new RegistryProject(targetProjectName, groupId);
            registryProject.create(customDirs);
        }

        //Create ESB Project
        if ("y".equalsIgnoreCase(createESBProject)) {
            targetProjectName = projectName + "ESB";
            modules.add(targetProjectName);
            System.out.println("Generating project :" + targetProjectName);
            ESBProject esbProject = new ESBProject(targetProjectName, groupId);
            esbProject.create();
        }


        //Create DSS project
        if ("y".equalsIgnoreCase(createDSSProject)) {
            targetProjectName = projectName + "DSS";
            modules.add(targetProjectName);
            System.out.println("Generating project :" + targetProjectName);
            DSSProject dssProject = new DSSProject(targetProjectName, groupId);
            dssProject.create();
        }

        //Create common CAPP project
        targetProjectName = projectName + "CAPP";
        modules.add(targetProjectName);
        System.out.println("Generating project :" + targetProjectName);
        CAPPProject cappProject = new CAPPProject(targetProjectName, groupId);
        cappProject.create();

        //Create CAAP Projects for envs
        for (String env : environments) {
            targetProjectName = projectName + "CAPP-" + env.toUpperCase();
            modules.add(targetProjectName);
            System.out.println("Generating project :" + targetProjectName);
            CAPPProject cappEnvProject = new CAPPProject(targetProjectName, groupId);
            cappEnvProject.create();
        }
        //Create Parent project
        RootProject rootProject = new RootProject(projectName, groupId, modules);
        rootProject.create();

    }
}
