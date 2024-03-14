package com.example.rainbow_six_battles;

public class Enemy {
    private int health;
    private int bps;
    private int cost;
    private int recharge;
    private String name;

    public Enemy(int health, int bps, int cost, int recharge, String name){
        this.health = health;
        this.bps = bps;
        this.cost = cost;
        this.name = name;
        this.recharge = recharge;

    }


}
