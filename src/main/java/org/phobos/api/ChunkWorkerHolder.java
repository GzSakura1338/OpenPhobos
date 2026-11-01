package org.phobos.api;

import org.phobos.core.task.TaskQueue;

public interface ChunkWorkerHolder {
   TaskQueue getHoleChunkWorker();

   TaskQueue getGraphChunkWorker();
}
