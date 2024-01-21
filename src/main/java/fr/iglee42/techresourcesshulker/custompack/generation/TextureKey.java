package fr.iglee42.techresourcesshulker.custompack.generation;

public record TextureKey(String key,String object) {

    public String toJson(){
        return "        \""+key+"\": \""+object+"\"";
    }
}
