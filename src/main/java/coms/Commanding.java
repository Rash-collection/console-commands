/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package coms;

/**
 * <p>Default command tree template, Mainly prepared behavior for {@link coms.Console}</p>
 * <p>
 * Not final for easy extension, although the whole thing were made specifically
 * to initialize {@code Command} and {@code CommandTree} objects externally.
 * </p>
 * @see     coms.Com
 * @see     coms.Commander
 * @see     coms.Command
 * @see     coms.CommandTree
 * @author rash4
 */
public class Commanding implements Commander{
    /**
     * <p>
     * This empty constructor totally depends on the once 
     * predefined {@code Console} static instance.
     * </p>
     * @see     coms.Console#VIRTUAL_CONSOLE
     */
    protected Commanding(){this(Console.VIRTUAL_CONSOLE);}
    protected Commanding(Console cons){
        this.CMDS = new CommandTree();
        this.console = cons;
    }
    @Override public final boolean execute(String command) {
        if(command == null)return false;
        this.console.appendl(command);
        final var parts = parts(command);
        final String pt1;
        final String remainings = (parts.length > 1) ? parts[1] : null;
        final Com com;
        if(parts.length > 0){
            pt1 = parts[0].toLowerCase();
            com = this.CMDS.getCom(pt1);
            if(com == null){
                this.console.appendl(">> Command not found... \n  - write 'help' to get the main commands list.");
                return false;
            }return switch(com){
                case Command cmd -> cmd.com().execute(remainings);
                case CommandTree sub -> {
                    if(remainings == null){
                        if(sub.hasDirectCom())yield sub.directCommand().com().execute("");
                        yield false;
                    }
                    yield this.checkNest(remainings, sub);
                }
            };
        }
        return false;
    }
    protected final boolean checkNest(String command, CommandTree next){
        final var parts = parts(command);
        final String pt1;
        final String remainings = (parts.length > 1) ? parts[1] : null;
        final Com com;
        if(parts.length > 0){
            pt1 = parts[0].toLowerCase();
            com = next.getCom(pt1);
            if(com == null){
                this.console.appendl(">> Command not found... \n  - write 'help' to get the main commands list.");
                return false;
            }return switch(com){
                case Command cmd -> cmd.com().execute(remainings);
                case CommandTree tree -> {
                    if(remainings == null){
                        if(tree.hasDirectCom())yield tree.directCommand().com().execute("");
                        yield false;
                    }
                    yield this.checkNest(remainings, tree);
                }
            };
        }
        return false;
    }
    public boolean fullHelpList(String bleh){
        this.console.appendl(">> Current MAIN commands -> list : ");
        this.CMDS.listHelp("   ", this.console);
        console.appendl(">> List-End <<||");
        return true;
    }
    public boolean getHelp(String pleh){// for the sake of the method's reference
        console.appendl(">> Current MAIN commands -> list : ");
        for(String como : this.CMDS.getList()){
            console.appendl("  --" + como + ".");
        }
        console.appendl(">> List-End <<||");
        return true;
    }
    public Commanding addCommand(Com neo, String name){
        if(neo == null || name == null || name.isBlank())return this; // fast escape.
        switch(neo){
            case CommandTree tree-> this.CMDS.setCom(name, tree);
            case Command cmd-> this.CMDS.setCom(name, cmd);
        }return this;
    }
    protected Console getConsole(){return this.console;}
    /**
     * <p>
     * Global static function to be used externally (as utility)
     * whenever or wherever needed.
     * </p>
     * <p>
     * example use :<br>
     * let's say we've got this statement -> ("give me money"), the 
     * {@link coms.Commanding#execute(java.lang.String)} gets the first part "give"
     * as an argument, but it's not removed from the string,
     * and here comes the use of this {@code parts} function.<br>
     * inside the recursive call, pass this method as an argument,
     * and for it's parameter pass the original String.<br>
     * it will return -> "me money"<br>
     * then use the "me" part and again return "money"<br>
     * in general, it's repeated <i>SAFELY</i> even when unexpectedly empty.<br>
     * after all it depends on the {@code CommandTree}'s branches initialization.
     * </p>
     * @param series the String to split into array using white space as splitter.
     * @return          
     * <p>
     * an array of 'words' with the first 'part(word) removed for next recursion.<br>
     * <b>PS :</b> {@code return ""} aka empty string if null. 
     * </p>
     */
    public final static String[] parts(String series){
        return (series == null ? "" : series).trim().split("\\s+", 2);
    }
    private final CommandTree CMDS;
    private final Console console;
}