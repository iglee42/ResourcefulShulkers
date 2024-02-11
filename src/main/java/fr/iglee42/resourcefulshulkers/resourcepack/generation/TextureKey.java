package fr.iglee42.resourcefulshulkers.resourcepack.generation;

public record TextureKey(String key,String object) {

    public String toJson(){
        return "        \""+key+"\": \""+object+"\"";
    }
}
