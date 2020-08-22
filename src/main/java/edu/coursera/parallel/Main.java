package edu.coursera.parallel;

import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        int mx = 80000000;
        double[] inputs = new double[mx];
        Random rd = new Random();
        for (int i=0;i<mx;i++)
            inputs[i] = rd.nextDouble();

        System.out.println(ReciprocalArraySum.seqArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(inputs,30) +"\n" );

        System.out.println(ReciprocalArraySum.seqArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(inputs,30) +"\n" );

        System.out.println(ReciprocalArraySum.seqArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(inputs,30) +"\n" );

        System.out.println(ReciprocalArraySum.seqArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(inputs,30) +"\n" );

        System.out.println(ReciprocalArraySum.seqArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parArraySum(inputs)  );
        System.out.println(ReciprocalArraySum.parManyTaskArraySum(inputs,30) +"\n" );



    }
}
