package test_dep;

public class Main {
    public static void main(String[] args) {
        SamplePieChart demo = new SamplePieChart("Comparison", "Which operating system are you using?");
        demo.pack();
        demo.setVisible(true);
    }
}
