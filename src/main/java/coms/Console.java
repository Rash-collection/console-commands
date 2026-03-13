/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package coms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * <p>Virtual Console</p>
 * 
 * @see     coms.Command
 * @see     coms.CommandTree
 * @see     coms.Commanding
 * @author rash4
 */
public class Console{
    protected Console(){
        this.COMMONS = new ArrayList<>(21);
    }
    public void initialize(boolean constructContainer){
        // early skip (if not new or not disposed properly)
        if(this.initialized)return;
        this.stats   = true; // the opposite of this is this activy
        this.write   = new JTextField();
        this.reads   = new JTextArea();
        this.rolls   = new JScrollPane(this.reads);
        this.write.addKeyListener(new Hots());
        this.reads.addKeyListener(new KeyAdapter(){
            @Override public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_ALT -> Console.this.write.requestFocus();
                }
            }
        });
        this.setStats(!this.stats);
        this.reads.setFont(this.devfon);
        this.reads.setForeground(this.foreground);
        this.reads.setBackground(this.background);
        this.reads.setLineWrap(true);
        this.reads.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
        this.reads.setWrapStyleWord(true);
        this.rolls.setViewportView(this.reads);
        this.rolls.setFocusable(true);
        this.commander = this::commands;
        //just tempo taboo XD -> comments below - just as a REMINDERs.
        this.reads.setFocusable(false); // true to select it's text
        this.reads.setEditable(false); // true only affect editing the text
        
        if(this.defaulted = constructContainer) this.defaultWindow();
        this.initialized = true;
    }
    public void initialize(){
        this.initialize(this.defaulted = false);
    }
    protected void defaultWindow(){
        final var panel = new JPanel();
        final var frame = new JFrame();
        
        panel.setLayout(null);
        panel.addComponentListener(new ComponentListener(){
            @Override public void componentResized(ComponentEvent e) {
                Console.this.resizing(panel.getSize());
            }
            @Override public void componentMoved(ComponentEvent e) {}
            @Override public void componentShown(ComponentEvent e) {}
            @Override public void componentHidden(ComponentEvent e) {}
        });
        panel.setPreferredSize(new Dimension(720, 680));
        this.setup(panel);
        
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        // must be overriden or just the dispose() method.
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        
        this.setStats(true);
        frame.setVisible(true);
    }
    /**The minimal changes to add to the panel*/
    public final Console setup(JPanel pane){ // remember to change
        pane.add(this.rolls);
        pane.add(this.write);
        this.resizing(pane.getSize());
        return this;
    }
    /**<p>as in whether they're visible and enabled. {@code ture} is the brought up.</p>*/
    public boolean isLive(){return this.stats;}
    public synchronized Console setStats(boolean updown){
        if(this.stats == updown) return this;
        this.stats = updown;
        this.write.setVisible(updown);
        this.write.setEnabled(updown);
        this.reads.setVisible(updown);
        this.reads.setEnabled(updown);
        this.reads.setFocusable(updown);
        this.rolls.setVisible(updown);
        this.rolls.setEnabled(updown);
        if(updown)SwingUtilities.invokeLater(this.write::requestFocusInWindow);
        return this;
    }
    public void resizing(Dimension containerSize){
        final int mar = 32, par = mar * 2, wid = containerSize.width - par;
        this.write.setBounds(mar, containerSize.height - (mar * 3), wid, mar);
        this.rolls.setBounds(mar, mar, wid, containerSize.height - (mar * 5));
    }
    public void clear(){this.reads.setText("");}
    public void append(String text){
        if(text == null)return;
        this.reads.append(text);
        // maybe will turn chars into an instance field for future!
        final int chars = this.reads.getDocument().getLength();
        this.reads.setCaretPosition(chars); // scrolls to end automatically
    }
    public Console appends(String text){
        this.append(text);
        return this;
    }
    public Console appendl(String text){
        return this.appends(text + "\n");
    }
    public Console submitCom(){
        final String input = this.write.getText();
        if (input == null) return this;
        this.submitCom(input);
        this.write.setText("");
        return this;
    }
    public Console submitCom(String command) {
        if (this.commander.execute(command)) {
            int idx = this.COMMONS.indexOf(command.toLowerCase());
            if (idx == -1) {
                this.COMMONS.add(command.toLowerCase());
                this.currentIndex = (idx = COMMONS.size()) - 1;
                this.appendl((">> command (%03d) has been saved.").formatted(idx));
            } else  this.currentIndex = idx;
        }return this;
    }
    /**<p>initial defined commands switch. simple just for convenience.</p>*/
    private boolean commands(String command){
        if(command == null) return false;
        this.appendl(command);//regardless WTF is the command  <-/0\->
        final var parts = command.trim().split("\\s+");
        final int lngt = parts.length;
        if(lngt < 1)return false;
        final var first = parts[0];
        switch(first.toLowerCase()){
            case "help" -> {}
            case "exit" -> {}
            case "clear"-> Console.this.clear();
            default     -> {
                this.appendl(">> Undefined command.");
                return false;
            }
        }
        return true;
    }
    public void setCommands(Commander cmd){
        if(cmd == null)return;
        this.commander = cmd;
    }
    protected void commonsRoll(int crement){//for now it's an infinity ring is also good
        final int lngt = this.COMMONS.size();
        if(lngt == 0)return;
        this.currentIndex = ((this.currentIndex + crement) % lngt + lngt) % lngt;
        this.write.setText(this.COMMONS.get(this.currentIndex));
    }
    public final class Hots implements KeyListener{
        private Hots(){}
        @Override public void keyTyped(KeyEvent e) {
        }
        @Override public void keyReleased(KeyEvent e) {
        }
        @Override public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
//                case KeyEvent.VK_ESCAPE ->  Launcher.dispose();
//                case KeyEvent.VK_ALT    ->  Launcher.focus();
                case KeyEvent.VK_ENTER  ->  Console.this.submitCom();
                case KeyEvent.VK_UP     ->  Console.this.commonsRoll(-1);//oldest
                case KeyEvent.VK_DOWN   ->  Console.this.commonsRoll(+1);//newest
                case KeyEvent.VK_DELETE ->  {
                    if(e.isControlDown())   Console.this.clear();
                    else Console.this.write.setText("");
                }
            }
        }
    }
    // these finals are area's settings of sort, soon will globalize them, to add to PERSONALIZE object.
    private final Font devfon = new Font("monospace", Font.PLAIN, 17);
    // both are for the area->reads
    private final Color background = new Color(74, 74, 74, 255), 
            foreground = new Color(10, 255, 20);
    private boolean defaulted = false, initialized = false;
    private boolean stats;
    private int currentIndex = -1;
    private final ArrayList<String> COMMONS;
    
    // to easily diffirentate their behaviors basedd on instance's name-sense.//
    /**The text-inputs -> one-line.*/
    private volatile JTextField write;
    /**The text-outputs -> multi-lines.*/
    private volatile JTextArea  reads;
    
    private Commander commander;
    private JScrollPane rolls;
    /**<p>Only one virtual console object, it's for debugging..etc.</p>*/
    final static Console VIRTUAL_CONSOLE;
    static{
        VIRTUAL_CONSOLE = new Console();
    }
}