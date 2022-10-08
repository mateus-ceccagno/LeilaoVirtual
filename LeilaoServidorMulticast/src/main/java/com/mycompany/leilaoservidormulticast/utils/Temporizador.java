/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.leilaoservidormulticast.utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;

/**
 *
 * @author lucas
 */
public class Temporizador {
    
    public Timer timer = new Timer();
    
    public int segundos = 240;
    public TimerTask task = new TimerTask() {
        
        @Override
        public void run() {
              
            if(segundos > 0){
                segundos--;
                System.out.println(segundos + " segundos restantes"); 
            }   
            
           if(segundos == 0){
                task.cancel();          
            }
            
        }
    };


}


