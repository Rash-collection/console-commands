/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package coms;

import java.util.HashMap;

/**
 * <p>The Tree of multiple commands.</p>
 * <p>- can have one direct command (direct leaf).</p>
 * @see     coms.Commander
 * @see     coms.Com
 * @see     coms.Command
 * @author rash4
 */
public final class CommandTree implements Com{ 
    public CommandTree(){this.CMDS = new HashMap<>();}
    public CommandTree setCom(String name, Com com){
        if(name == null || com == null){}
        else if(com instanceof Command single){
            if(single.com() == null)return this;
            this.CMDS.putIfAbsent(name, single);
        }else if(com instanceof CommandTree list){
            if(list.CMDS.isEmpty())return this;
            this.CMDS.putIfAbsent(name, list);
        }
        return this;
    }
    public void listHelp(String prefix, Console cons) {
        // List the direct command for this tree/branch if it exists
        if (this.direct != null) {
            cons.appendl(prefix + "Direct command: " + this.direct.getClass().getSimpleName());
        }
        // List command-trees within this tree/branch
        for (String key : this.CMDS.keySet()) {
            Com com = this.CMDS.get(key);
            switch (com) {
                case CommandTree commandTree -> {
                    // If it's a commandTree, recursively list it's help.
                    cons.appendl(prefix + "--" + key + " (SubCommand):");
                    commandTree.listHelp(prefix + "  ", cons);
                }
                // If it's a single Command (end node/leaf), just print it out.
                case Command cmd -> 
                    cons.appendl(prefix + "--" + key + " (Command)");
            }
        }
    }
    public boolean hasDirectCom(){return this.direct != null;}
    @Override public boolean hasParam(){return true;}
    public CommandTree setDirectCom(Command com){
        this.direct = com;
        return this;
    }
    public String[] getList(){return this.CMDS.keySet().toArray(String[]::new);}
    public Com getCom(String command){return this.CMDS.get(command);}
    public Command directCommand(){return this.direct;}
    private Command direct;
    private final HashMap<String, Com> CMDS;
}