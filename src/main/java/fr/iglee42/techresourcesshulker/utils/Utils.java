package fr.iglee42.techresourcesshulker.utils;

import net.minecraft.network.FriendlyByteBuf;

public class Utils {


    public static void saveStringToBuf(FriendlyByteBuf buf ,String s){
        buf.writeInt(s.length());
        for (char c : s.toCharArray()){
            buf.writeChar(c);
        }
    }

    public static String readStringFromBuf(FriendlyByteBuf buf){
        int length = buf.readInt();
        char[] word = new char[length];
        for (int i = 0; i < length; i++){
            word[i] = buf.readChar();
        }
        return String.valueOf(word);
    }


}
