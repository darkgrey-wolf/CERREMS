/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgExceptions;

/**
 *
 * @author GreyWolf
 */
public class PasswordMismatchException extends Exception {
    public PasswordMismatchException(String value){
        super(value);
    }
}
