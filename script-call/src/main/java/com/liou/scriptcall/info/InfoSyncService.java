package com.liou.scriptcall.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.window.Application;

public class InfoSyncService {

    class SyncResult implements Runnable {

        private InfoSyncable syncable;

        public SyncResult(InfoSyncable syncable) {
            this.syncable = syncable;
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(syncable.getInputStream(), Application.defaultCharset.getCharset()));
            String line = null;
            try {
                while (null != (line = br.readLine())) {
                    syncable.getMessagePipeline().append(line);
                }
            } catch (Exception e) {
                logger.error("syncResult", e);
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

    }

    class SyncError implements Runnable {

        private InfoSyncable syncable;

        public SyncError(InfoSyncable syncable) {
            this.syncable = syncable;
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(syncable.getErrorStream(), Application.defaultCharset.getCharset()));
            String line = null;
            try {
                while (null != (line = br.readLine())) {
                    syncable.getMessagePipeline().append(line);
                }
            } catch (Exception e) {
                logger.error("syncResult", e);
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

    }

    private static Logger logger = LoggerFactory.getLogger(InfoSyncService.class);

    public int syncInfo(InfoSyncable syncable) {
        Thread syncInfo = new Thread(new SyncResult(syncable));
        syncInfo.setName("sync info for " + syncable.syncName());
        syncInfo.start();
        Thread syncError = new Thread(new SyncError(syncable));
        syncError.setName("sync error for " + syncable.syncName());
        syncError.start();
        int result = syncable.currentWait();
        bye(syncable);
        return result;
    }

    protected void bye(InfoSyncable syncable) {
        try {
            syncable.getMessagePipeline().append("\nbye");
        } catch (IOException e) {
            logger.error("bye", e);
        }
    }

}
