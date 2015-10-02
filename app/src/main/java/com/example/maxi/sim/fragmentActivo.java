package com.example.maxi.sim;

/**
 * Created by yamila on 27/09/2015.
 */
public class fragmentActivo {

        private static fragmentActivo instance;

        // Global variable
        private String data;

        // Restrict the constructor from being instantiated
        private fragmentActivo(){}

        public void setData(String d){
            this.data=d;
        }
        public String getData(){
            return this.data;
        }

        public static synchronized fragmentActivo getInstance(){
            if(instance==null){
                instance=new fragmentActivo();
            }
            return instance;
        }
}
