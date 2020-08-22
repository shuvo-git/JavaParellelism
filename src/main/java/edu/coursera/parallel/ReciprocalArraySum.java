package edu.coursera.parallel;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {
    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        long startTime = System.nanoTime();
        double sum = 0;
        // Compute sum of reciprocals of array elements
        for (int i = 0; i < input.length; i++) {
            sum += 1 / input[i];
        }

        long totTime = System.nanoTime() - startTime;
        //System.out.print((totTime/1e6)+" ms  ");

        return sum;
    }

    /**
     * Computes the size of each chunk, given the number of chunks to create
     * across a given number of elements.
     *
     * @param nChunks The number of chunks to create
     * @param nElements The number of elements to chunk across
     * @return The default chunk size
     */
    private static int getChunkSize(final int nChunks, final int nElements) {
        // Integer ceil
        return (nElements + nChunks - 1) / nChunks;
    }

    /**
     * Computes the inclusive element index that the provided chunk starts at,
     * given there are a certain number of chunks.
     *
     * @param chunk The chunk to compute the start of
     * @param nChunks The number of chunks created
     * @param nElements The number of elements to chunk across
     * @return The inclusive index that this chunk starts at in the set of
     *         nElements
     */
    private static int getChunkStartInclusive(final int chunk,
            final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        return chunk * chunkSize;
    }

    /**
     * Computes the exclusive element index that the provided chunk ends at,
     * given there are a certain number of chunks.
     *
     * @param chunk The chunk to compute the end of
     * @param nChunks The number of chunks created
     * @param nElements The number of elements to chunk across
     * @return The exclusive end index for this chunk
     */
    private static int getChunkEndExclusive(final int chunk, final int nChunks,
            final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }

    /**
     * This class stub can be filled in to implement the body of each task
     * created to perform reciprocal array sum in parallel.
     */
    private static class ReciprocalArraySumTask extends RecursiveAction {
        /**
         * Starting index for traversal done by this task.
         */
        private final int lo;
        /**
         * Ending index for traversal done by this task.
         */
        private final int hi;
        /**
         * Input array to reciprocal sum.
         */
        private final double[] input;
        /**
         * Intermediate value produced by this task.
         */
        private double value;

        private final int THRESHOLD  = 50000;
        private final int nTask;

        /**
         * Constructor.
         * @param setStartIndexInclusive Set the starting index to begin
         *        parallel traversal at.
         * @param setEndIndexExclusive Set ending index for parallel traversal.
         * @param setInput Input values
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive,
                final int setEndIndexExclusive, final double[] setInput,final int nTask) {
            this.lo = setStartIndexInclusive;
            this.hi = setEndIndexExclusive;
            this.input = setInput;
            this.nTask = nTask;
        }



        /**
         * Getter for the value produced by this task.
         * @return Value produced by this task
         */
        public double getValue() {
            return value;
        }

        @Override
        protected void compute() {
            // TODO
            if(hi-lo <= THRESHOLD)
            {
                double sum = 0;
                for(int i=lo;i<hi;i++)
                    sum += 1.0/input[i];
                this.value = sum;
            }
            else
            {
                /*int mid = (lo+hi)/nTask;


                ReciprocalArraySumTask left  = new ReciprocalArraySumTask(lo,mid,input,nTask);
                ReciprocalArraySumTask right = new ReciprocalArraySumTask(mid,hi,input,nTask);*/

                ArrayList<ReciprocalArraySumTask> tasks = new ArrayList<ReciprocalArraySumTask>();

                //int chunk = (hi+lo)/nTask+1,i=0;
                int chunk = (hi - lo )/nTask+1,i=0;

                //System.out.println("Range : "+lo+" - "+hi+" Chunk: "+ chunk);
                int chunkLow = lo;
                for(i=0;i<nTask-1;i++)
                {
                    ReciprocalArraySumTask tmp = new ReciprocalArraySumTask(
                            chunkLow,
                            (chunkLow+chunk),
                            input,
                            nTask);

                    tasks.add(tmp);

                    //System.out.println(chunkLow+" "+(chunkLow+chunk) );
                    chunkLow += chunk;

                }
                //int end = (chunkLow+chunk)>hi? hi:chunkLow;
                tasks.add(i,new ReciprocalArraySumTask(chunkLow,
                        hi,
                        input,
                        nTask));
                //System.out.println(chunkLow+" "+hi+"\n\n");



                for(i=0;i<nTask-1;i++)
                {
                    tasks.get(i).fork();

                }

                //compute rightmost and get val
                tasks.get(i).compute();



                //join other Task
                for(i=0;i<nTask-1;i++)
                    tasks.get(i).join();



                /*left.fork();
                right.compute();
                left.join();*/

                // sum up all returned value from all tasks
                for(i=0;i<nTask-1;i++)
                    this.value += tasks.get(i).getValue();
                this.value += tasks.get(i).getValue();
                //this.value = left.value + right.value;

            }
        }
    }




    /**
     * TODO: Modify this method to compute the same reciprocal sum as
     * seqArraySum, but use two tasks running in parallel under the Java Fork
     * Join framework. You may assume that the length of the input array is
     * evenly divisible by 2.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double parArraySum(final double[] input) {
        assert input.length % 2 == 0;
        long startTime = System.nanoTime();



        //ForkJoinPool forkJoinPool = new ForkJoinPool();

        ReciprocalArraySumTask task0 = new ReciprocalArraySumTask(0,input.length,input,2);
        ForkJoinPool.commonPool().invoke(task0);




        long totTime = System.nanoTime() - startTime;
        //System.out.print((totTime/1e6)+" ms  ");

        return task0.value;
    }

    /**
     * TODO: Extend the work you did to implement parArraySum to use a set
     * number of tasks to compute the reciprocal array sum. You may find the
     * above utilities getChunkStartInclusive and getChunkEndExclusive helpful
     * in computing the range of element indices that belong to each chunk.
     *
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input,
            final int numTasks) {
        long startTime = System.nanoTime();



        //ForkJoinPool forkJoinPool = new ForkJoinPool();

        ReciprocalArraySumTask task0 = new ReciprocalArraySumTask(0,input.length,input,numTasks);
        ForkJoinPool.commonPool().invoke(task0);




        long totTime = System.nanoTime() - startTime;
        //System.out.print((totTime/1e6)+" ms  ");

        return task0.value;
    }
}