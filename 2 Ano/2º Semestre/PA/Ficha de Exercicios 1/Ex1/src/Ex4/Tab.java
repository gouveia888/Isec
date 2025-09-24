package Ex4;

public class Tab {
    private static final int TAM = 20; //constante

    private final int[] tab;

    private int nElemesInTab, nGeneratedElems; //valores iniciais

    public Tab() {
        tab = new int [TAM];
        nElemesInTab = 0;
        nGeneratedElems = 0;
    }

    public void generate() {
        for(int i = 0; i < TAM; i++) {
            int num = (int) (Math.random() * 100 + 1);
            if(i == 0) {
                tab[i] = num;
                nElemesInTab++;
            }
            if(i>=1) {
                    if (checkarray()) {
                        tab[nElemesInTab++] = num;
                }
            }
        }
    }
    public boolean checkarray() {
        for(int i = 1; i < nElemesInTab; i++) {
            if(tab[i] == tab[i-1]) {
                return false;
            }
        }
        return true;
    }

    public void mostra() {
        System.out.print("[");
        for(int i = 0; i < nElemesInTab; i++) {
            System.out.print(" " + tab[i]);
        }
        System.out.println("]");
    }
}
