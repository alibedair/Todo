package com.fcai.SoftwareTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoController;
import com.fcai.SoftwareTesting.todo.TodoCreateRequest;
import com.fcai.SoftwareTesting.todo.TodoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoControllerTest {
    @Mock
    private TodoServiceImpl todoService;
    @InjectMocks
    private TodoController todoController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(todoController).build();
        this.objectMapper = new ObjectMapper();
    }
    @Test
    public void testReadTodo() throws Exception {
        Todo todo = new Todo("1","Testing","testing units",false);
        when(todoService.read(todo.getId())).thenReturn(todo);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/read")
                                .param("id", todo.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
               // .andExpect(content().json("{\"id\":\"1\",\"title\":\"Testing\",\"description\":\"testing units\",\"completed\":false}"));
                .andExpect(content().json(objectMapper.writeValueAsString(todo)));
    }
    @Test
    public void testReadTodowithInvalidpath() throws Exception {
        Todo todo = new Todo("1","Testing","testing units",false);
        when(todoService.read(todo.getId())).thenReturn(todo);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/readd")
                                .param("id", todo.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testReadTodowithinvalidHTTPRequest() throws Exception {
        Todo todo = new Todo("1","Testing","testing units",false);
        when(todoService.read(todo.getId())).thenReturn(todo);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/read")
                                .param("id", todo.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }
    @Test
    public void testReadTodoNull() throws Exception {
        // wrong id will lead to null Todo object its same test case not another test case as white box testing
        Todo todo = new Todo();
        when(todoService.read(todo.getId())).thenThrow(new IllegalArgumentException());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/read")
                                .param("id", todo.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testReadTodoempty() throws Exception {
        Todo todo = new Todo();
        todo.setId("");

        when(todoService.read(todo.getId())).thenThrow(new IllegalArgumentException());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/read")
                                .param("id", todo.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateTodo() throws Exception {
        Todo todo = new Todo("1", "Testing", "test a system", false);

        String todoCreateRequestJson = "{\"title\":\"Testing\",\"description\":\"test a system\"}";

        when(todoService.create(Mockito.any(TodoCreateRequest.class))).thenReturn(todo);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoCreateRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               // .andExpect(content().json("{\"id\":\"1\",\"title\":\"Testing\",\"description\":\"test a system\",\"completed\":false}"));
                 .andExpect(content().json(objectMapper.writeValueAsString(todo)));
    }
    @Test
    public void testCreateTodowithEmptytitle() throws Exception{

        String invalidRequestJson = "{\"title\":\"\",\"description\":\"test\",\"completed\":false}";

        when(todoService.create(Mockito.any(TodoCreateRequest.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateTodowithEmptydescription() throws Exception{

        String invalidRequestJson = "{\"title\":\"test\",\"description\":\"\",\"completed\":false}";

        when(todoService.create(Mockito.any(TodoCreateRequest.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateTodoNull() throws Exception{

        String invalidRequestJson = "";

        when(todoService.create(null)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateTodoWithInvalid() throws Exception {
        Todo todo = new Todo("1", "Testing", "test a system", false);

        String todoCreateRequestJson = "{\"title\":\"Testing\",\"description\":\"test a system\"}";

        when(todoService.create(Mockito.any(TodoCreateRequest.class))).thenReturn(todo);

        mockMvc.perform(post("/createe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoCreateRequestJson))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testupdate() throws Exception{
        // passing invalid id is useless test case as tested before in read test case
        Todo todo = new Todo("1","Testing","testing units",true);
        Mockito.when(todoService.update(todo.getId(),true)).thenReturn(todo);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/update")
                                .param("id", todo.getId())
                                .param("completed",String.valueOf(todo.isCompleted()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
              //  .andExpect(content().json("{\"id\":\"1\",\"title\":\"Testing\",\"description\":\"testing units\",\"completed\":true}"))
                .andExpect(content().json(objectMapper.writeValueAsString(todo)));
    }
    @Test
    public void testupdatewithPassingNull() throws Exception{
        Todo todo = new Todo();
        when(todoService.update(todo.getId(),true)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/update")
                                .param("id", todo.getId())
                                .param("completed",String.valueOf(todo.isCompleted()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testupdatewithInvalidPath() throws Exception{
        Todo todo = new Todo("1","Testing","testing units",true);
        when(todoService.update(todo.getId(),true)).thenReturn(todo);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/updatee")
                                .param("id", todo.getId())
                                .param("completed",String.valueOf(todo.isCompleted()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testupdatewithinvalidCompletion() throws Exception{
        Todo todo = new Todo("1","Testing","testing units",true);
        Mockito.when(todoService.update(todo.getId(),true)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/update")
                                .param("id", todo.getId())
                                .param("completed","truee")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testdelete()throws Exception{
        Todo todo = new Todo("1","Testing","testing units",false);
        Mockito.doNothing().when(todoService).delete(todo.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete")
                        .param("id",todo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(todoService, Mockito.times(1)).delete(todo.getId());

    }
    @Test
    public void testdeletewithemptyid() throws Exception{
        Todo todo = new Todo() ;
        todo.setId("");
        Mockito.doThrow(new IllegalArgumentException()).when(todoService).delete(todo.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete")
                        .param("id",todo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(todoService, Mockito.times(1)).delete(todo.getId());
    }

    @Test
    public void testdeletewithInvalidpath()throws Exception{
        Todo todo = new Todo("1","Testing","testing units",false);
        Mockito.doNothing().when(todoService).delete(todo.getId());
        mockMvc.perform(MockMvcRequestBuilders.delete("/deletee")
                        .param("id",todo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testTodosListNull() throws Exception{
        // service is not constructed so list is null
        Mockito.when(todoService.list()).thenThrow(new IllegalArgumentException());
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testTodosListWithInvalidPath() throws Exception{
        List<Todo> todoList = todoService.list();
        Mockito.when(todoService.list()).thenReturn(todoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/List")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testTodosEmptyList() throws Exception{
        List<Todo> todoList = new ArrayList<>();
        Mockito.when(todoService.list()).thenReturn(todoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));

    }
    @Test
    public void testTodosList() throws Exception{
        List<Todo> todoList = new ArrayList<>();
        Todo todo = new Todo("1","Testing","testing units",false);
        Todo todo2 = new Todo("2","Testing","testing units",false);
        Todo todo3 = new Todo("3","Testing","testing units",false);
        todoList.add(todo2);
        todoList.add(todo3);
        todoList.add(todo);
        Mockito.when(todoService.list()).thenReturn(todoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(content().json(objectMapper.writeValueAsString(todoList)));


    }
    @Test
    public void testListCompleted() throws Exception{
        List<Todo> complete = new ArrayList<>();
        Todo todo = new Todo("1","Testing","testing units",true);
        Todo todo2 = new Todo("2","Testing","testing units",true);
        Todo todo3 = new Todo("3","Testing","testing units",true);
        complete.add(todo);
        complete.add(todo2);
        complete.add(todo3);
        Mockito.when(todoService.listCompleted()).thenReturn(complete);
        mockMvc.perform(MockMvcRequestBuilders.get("/listCompleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(content().json(objectMapper.writeValueAsString(complete)));

    }
    @Test
    public void testTodosEmptyCompletedList() throws Exception{
        List<Todo> completed = new ArrayList<>();
        Mockito.when(todoService.listCompleted()).thenReturn(completed);
        mockMvc.perform(MockMvcRequestBuilders.get("/listCompleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));
    }
    @Test
    public void testListCompletedWithInvalidPath() throws Exception{
        List<Todo> complete = new ArrayList<>();
        Todo todo = new Todo("1","Testing","testing units",true);
        complete.add(todo);
        Mockito.when(todoService.listCompleted()).thenReturn(complete);
        mockMvc.perform(MockMvcRequestBuilders.get("/ListCompleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testlistCompletedNull() throws Exception{
        Mockito.when(todoService.listCompleted()).thenThrow(new IllegalArgumentException());
        mockMvc.perform(MockMvcRequestBuilders.get("/listCompleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

}

}