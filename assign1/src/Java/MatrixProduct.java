import java.util.Scanner;

public class MatrixProduct {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int lin, col, blockSize;
        int op;

        op = 1;
        do {
            System.out.println("\n1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.println("3. Block Multiplication");
            System.out.println("Selection?: ");
            op = scanner.nextInt();
            if (op == 0)
                break;
            System.out.print("Dimensions: lins=cols ? ");
            lin = scanner.nextInt();
            col = lin;
            double time = 0;
            switch (op) {
                case 1:
                    for (int i = 0; i < 10; i++){
                        double startTime1 = System.currentTimeMillis();
                        OnMult(lin, col);
                        double endTime1 = System.currentTimeMillis();
                        System.out.println("Time taken: " + (endTime1 - startTime1)/1000 + " seconds");
                        time += (endTime1 - startTime1)/1000;
                    }
                    
                    System.out.println("\nAverage time: " + time/10 + " seconds");
                    time = 0;
                    break;
                case 2:
                    for (int i = 0; i < 10; i++){
                        double startTime2 = System.currentTimeMillis();
                        OnMultLine(lin, col);
                        double endTime2 = System.currentTimeMillis();
                        System.out.println("Time taken: " + (endTime2 - startTime2)/1000 + " seconds");
                        time += (endTime2 - startTime2)/1000;
                    }
                    System.out.println("\nAverage time: " + time/10 + " seconds");
                    time = 0;
                    break;
                case 3:
                    System.out.print("Block Size? ");
                    blockSize = scanner.nextInt();
                    for (int i = 0; i < 10; i++){
                        double startTime3 = System.currentTimeMillis();
                        OnMultBlock(lin, col, blockSize);
                        double endTime3 = System.currentTimeMillis();
                        System.out.println("Time taken: " + (endTime3 - startTime3)/1000 + " seconds");
                        time += (endTime3 - startTime3)/1000;
                    }
                    System.out.println("\nAverage time: " + time/10 + " seconds");
                    time = 0;
                    break;
            }

        } while (op != 0);

        scanner.close();
    }

    public static void OnMult(int m_ar, int m_br) {
        double[] pha = new double[m_ar * m_ar];
        double[] phb = new double[m_ar * m_ar];
        double[] phc = new double[m_ar * m_ar];

        // Initialization of matrices pha and phb

        for (int i = 0; i < m_ar; i++) {
            for (int j = 0; j < m_ar; j++) {
                pha[i * m_ar + j] = 1.0;
            }
        }

        for (int i = 0; i < m_br; i++) {
            for (int j = 0; j < m_br; j++) {
                phb[i * m_br + j] = i + 1;
            }
        }


        // Matrix multiplication
        for (int i = 0; i < m_ar; i++) {
            for (int j = 0; j < m_br; j++) {
                double temp = 0;
                for (int k = 0; k < m_ar; k++) {
                    temp += pha[i * m_ar + k] * phb[k * m_br + j];
                }
                phc[i * m_ar + j] = temp;
            }
        }

        // Displaying result matrix phc
        System.out.println("Result matrix:");
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(phc[i * m_ar + j] + " ");
            }
            System.out.println();
        }
    }

    public static void OnMultLine(int m_ar, int m_br) {
        double[] pha = new double[m_ar * m_ar];
        double[] phb = new double[m_ar * m_ar];
        double[] phc = new double[m_ar * m_ar];

        // Initialization of matrices pha and phb

        for (int i = 0; i < m_ar; i++) {
            for (int j = 0; j < m_ar; j++) {
                pha[i * m_ar + j] = 1.0;
            }
        }

        for (int i = 0; i < m_br; i++) {
            for (int j = 0; j < m_br; j++) {
                phb[i * m_br + j] = i + 1;
            }
        }

        // Matrix multiplication
        for (int i = 0; i < m_ar; i++) {
            for (int k = 0; k < m_ar; k++) {
                for (int j = 0; j < m_br; j++) {
                    phc[i * m_ar + j] += pha[i * m_ar + k] * phb[k * m_br + j];
                }
            }
        }

        // Displaying result matrix phc
        System.out.println("Result matrix:");
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(phc[i * m_ar + j] + " ");
            }
            System.out.println();
        }
    }

    public static void OnMultBlock(int m_ar, int m_br, int bkSize) {
        double[] pha = new double[m_ar * m_ar];
        double[] phb = new double[m_ar * m_ar];
        double[] phc = new double[m_ar * m_ar];

        // Initialization of matrices pha and phb

        for (int i = 0; i < m_ar; i++) {
            for (int j = 0; j < m_ar; j++) {
                pha[i * m_ar + j] = 1.0;
            }
        }

        for (int i = 0; i < m_br; i++) {
            for (int j = 0; j < m_br; j++) {
                phb[i * m_br + j] = i + 1;
            }
        }

        // Matrix multiplication
        for (int bi = 0; bi < m_ar; bi += bkSize) {
            for (int bk = 0; bk < m_ar; bk += bkSize) {
                for (int bj = 0; bj < m_br; bj += bkSize) {
                    for (int i = bi; i < Math.min(bi + bkSize, m_ar); i++) {
                        for (int k = bk; k < Math.min(bk + bkSize, m_ar); k++) {
                            for (int j = bj; j < Math.min(bj + bkSize, m_br); j++) {
                                phc[i * m_ar + j] += pha[i * m_ar + k] * phb[k * m_br + j];
                            }
                        }
                    }
                }
            }
        }

        // Displaying result matrix phc
        System.out.println("Result matrix:");
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(phc[i * m_ar + j] + " ");
            }
            System.out.println();
        }
    }
}
