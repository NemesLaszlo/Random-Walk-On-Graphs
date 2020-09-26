package backend;

public class CustomVertex {
    private String id;
    private boolean visited;

    public CustomVertex(String id){
        this.id = id;
        this.visited = false;

    }

    public String getId(){
        return this.id;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    @Override
    public String toString(){
        return this.id;
    }
}
