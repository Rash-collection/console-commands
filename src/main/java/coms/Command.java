/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package coms;

/**
 * <p>The direct command (end of command tree AKA leaf-node)</p>
 * @see     coms.Commander
 * @see     coms.Com
 * @see     coms.CommandTree
 * @author rash4
 */
public final class Command implements Com{
    public Command(Commander cono){this.CONO = cono;}
    public Command(Commander cono, boolean para){
        this(cono);
        this.param = para;
    }
    public Commander com(){return this.CONO;}
    @Override public boolean hasParam(){return this.param;}
    public Command setParam(boolean param){
        this.param = param;
        return this;
    }
    private boolean param = false;
    private final Commander CONO;
}