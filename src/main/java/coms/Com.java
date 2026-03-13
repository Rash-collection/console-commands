/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package coms;

import static coms.Console.VIRTUAL_CONSOLE;
/**
 * <p>The root base class of all commands.</p>
 * @see     coms.Command
 * @see     coms.CommandTree
 * @author rash4
 */
sealed interface Com permits Command, CommandTree {
    /**for the list display in console.*/
    default boolean hasParam(){return false;}
    /**
     * Not ready yet.
     * @deprecated      Till it's clear how to use it or 'removed'.
     */
    @Deprecated(since="1.1-SNAPSHOT")
    static Console console(){
        return VIRTUAL_CONSOLE;
    }
}