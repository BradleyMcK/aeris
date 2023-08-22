package com.aeris.demo.model;

public record ConcentrationGrid(double time, double z, ConcentrationRow[] rows) {

    public static class ConcentrationRow {

        private final double y;
        private final double[] x;
        private final double[] concentrations;

        public ConcentrationRow(double y, int xSize) {
            this.y = y;
            this.x = new double[xSize];
            this.concentrations = new double[xSize];
        }

        public void addConcentration(int index, double x, double c) {
            this.x[index] = x;
            this.concentrations[index] = c;
        }

        public double getY() {
            return y;
        }

        public double[] getX() {
            return x;
        }

        public double[] getConcentrations() {
            return concentrations;
        }
    }

}
