package com.command.base.window;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.command.base.Charset;
import com.command.base.command.Command;
import com.command.base.config.Configuration;
import com.command.base.service.ServiceLocator;
import com.command.base.service.StoreService;

/**
 * 
 * 循环读取用户输入并处理<br>
 * 文件配置<br>
 * 
 * @author liou
 *
 */
public abstract class Application {

    public static final PrintStream console = System.out;
    public static final BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
    public static final String STORE_GLOBAL = "global";

    public static Charset defaultCharset = Charset.UTF8;

    private static List<Destroyable> destroyables = new ArrayList<>();

    private Worker worker;
    private static Vector<Worker> otherWorkers = new Vector<>();
    private volatile static int otherWorkerSize = 0;

    public Application(Worker Worker) {
        this.worker = Worker;
        if (!init()) {
            throw new RuntimeException("init failuer");
        }
        loadConfigs();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
    }

    public abstract boolean init();

    public abstract String[] getConfigFiles();

    public static String getConfig(String key) {
        StoreService storeService = ServiceLocator.getService(StoreService.class);
        Configuration global = storeService.getValue(STORE_GLOBAL, Configuration.class);
        return global.getPropertie(key);
    }

    private void loadConfigs() {
        String[] configFiles = getConfigFiles();
        if (configFiles == null || configFiles.length == 0) {
            return;
        }
        Configuration configuration = new Configuration();
        for (String configFile : configFiles) {
            configuration.load(configFile);
        }
        StoreService storeService = ServiceLocator.getService(StoreService.class);
        storeService.save(STORE_GLOBAL, configuration);
    }

    public void mainLoop() {
        String line = null;
        try {
            while (null != (line = systemIn.readLine())) {
                try {
                    line = line.trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    Command command = new Command(line);
                    if (otherWorkerSize > 0) {
                        Worker remove = otherWorkers.remove(0);
                        remove.execute(command);
                        otherWorkerSize = otherWorkers.size();
                    } else {
                        worker.execute(command);
                    }
                } catch (RuntimeException e) {
                    console.println(line + " no execute");
                    e.printStackTrace(console);
                }
            }
        } catch (Exception e) {
            console.println(e.getMessage());
        }
    }

    /**
     * 通过worker处理一条命令<br>
     * 不严格同步
     * 
     * @param worker
     */
    public static void processCommand(Worker worker) {
        otherWorkers.add(worker);
        otherWorkerSize = otherWorkers.size();
    }

    public static void registerDestroy(Destroyable destroyable) {
        destroyables.add(destroyable);
    }

    public void shutdown() {
        for (Destroyable destroyable : destroyables) {
            destroyable.destroy();
        }
    }

}