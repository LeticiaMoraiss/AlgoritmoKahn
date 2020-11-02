package algoritmokahn;

import java.util.*;

class Grafo {

    private final Map<Integer, Set<Integer>> adjList = new HashMap<Integer, Set<Integer>>();

    void Adicionar(int from, int... to) {
        if (!adjList.containsKey(from)) {
            adjList.put(from, new HashSet<Integer>());
        }

        for (int t : to) {
            adjList.get(from).add(t);
        }
    }

    Map<Integer, Set<Integer>> PegarListaAdjacente() {
        return adjList;
    }
}

class OrdenacaoTopologica {

    private final Grafo g;
    private HashMap<Integer, Integer> entrada;
    private Set<Integer> semLigacao;

    OrdenacaoTopologica(Grafo g) {
        this.g = g;
    }

    private void ComputarEntrada() {
        entrada = new HashMap<Integer, Integer>();
        semLigacao = new HashSet<Integer>();

        Map<Integer, Set<Integer>> a = g.PegarListaAdjacente();

        for (Map.Entry<Integer, Set<Integer>> e : a.entrySet()) {
            int cur = e.getKey();

            if (!entrada.containsKey(cur)) {
                semLigacao.add(cur);
            }

            for (int i : a.get(cur)) {
                semLigacao.remove(i);

                if (!entrada.containsKey(i)) {
                    entrada.put(i, 1);
                } else {
                    entrada.put(i, entrada.get(i) + 1);
                }
            }
        }
    }

    List<Integer> PegarOrdenacao() {
        ComputarEntrada();

        if (semLigacao.size() == 0) {
            throw new RuntimeException("Grafo com ciclos");
        }

        Map<Integer, Set<Integer>> a = g.PegarListaAdjacente();
        List<Integer> l = new LinkedList<Integer>();

        while (!semLigacao.isEmpty()) {
            int cur = semLigacao.iterator().next();
            semLigacao.remove(cur);

            l.add(cur);

            for (int i : a.get(cur)) {
                int cnt = entrada.get(i);

                int newCnt = cnt - 1;
                entrada.put(i, newCnt);

                if (newCnt == 0) {
                    semLigacao.add(i);
                }
            }
        }

        if (l.size() != a.size()) {
            throw new RuntimeException("Grafo com ciclos");
        }
        return l;
    }
}

public class AlgoritmoKahn {

    public static void main(String[] args) {
        int custo = 0;
        Grafo g = new Grafo();
        g.Adicionar(5, 11);
        g.Adicionar(7, 11, 8);
        g.Adicionar(3, 8, 10);
        g.Adicionar(11, 2, 9, 10);
        g.Adicionar(8, 9);
        g.Adicionar(2);
        g.Adicionar(9);
        g.Adicionar(10);

        OrdenacaoTopologica t = new OrdenacaoTopologica(g);

        try {
            List<Integer> l = t.PegarOrdenacao();

            System.out.print("\n Ordenação topologica: ");

            for (int i : l) {
                System.out.print(i + " ");
                custo = custo + 1;
            }
            System.out.println("\n Custo maximo: " + (custo - 1) + "\n\n");

            System.out.println();
        } catch (Exception e) {
            System.out.println("Falha ao construir ordenação topologica do Grafo: " + e);
        }
    }
}
