package com.fcai.SoftwareTesting;


import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoCreateRequest;
import com.fcai.SoftwareTesting.todo.TodoService;
import com.fcai.SoftwareTesting.todo.TodoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TodoServiceTest {
    private TodoService todoService;

    @Before
    public void setUp() {
        todoService = new TodoServiceImpl();
    }
        @Test
        public void testCreateTodoSuccessfully() {
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("Title", "Description");
            Todo todo = todoService.create(todoCreateRequest);
            Assertions.assertNotNull(todo);
            Assertions.assertEquals(todoCreateRequest.getTitle(), todo.getTitle());
            Assertions.assertEquals(todoCreateRequest.getDescription(), todo.getDescription());
            Assertions.assertFalse(todo.isCompleted());
        }

        @Test
        public void testCreateTodoWithNullRequest() {
            // When and Then
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                todoService.create(null);
            });
        }

        @Test
        public void testCreateTodoWithEmptyTitle() {
            // Given
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("", "Description");
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                todoService.create(todoCreateRequest);
            });
        }
        @Test
        public void testCreateTodoWithEmptyDescription() {
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("Title", "");
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                todoService.create(todoCreateRequest);
            });
        }
        @Test
        public void testVerifyIdGeneration() {
            // Given
            TodoCreateRequest todoCreateRequest1 = new TodoCreateRequest("Title 1", "Description 1");
            TodoCreateRequest todoCreateRequest2 = new TodoCreateRequest("Title 2", "Description 2");
            Todo todo1 = todoService.create(todoCreateRequest1);
            Todo todo2 = todoService.create(todoCreateRequest2);
            Assertions.assertNotNull(todo1.getId());
            Assertions.assertNotNull(todo2.getId());
            Assertions.assertNotEquals(todo1.getId(), todo2.getId());
        }
        @Test
        public void testUniqueID(){
            TodoCreateRequest todoCreateRequest1 = new TodoCreateRequest("Title 1", "Description 1");
            TodoCreateRequest todoCreateRequest2 = new TodoCreateRequest("Title 2", "Description 2");
            Todo todo1 = todoService.create(todoCreateRequest1);
            Todo todo2 = todoService.create(todoCreateRequest2);
            todoService.delete(todo1.getId());
            TodoCreateRequest todoCreateRequest3 = new TodoCreateRequest("Title 3", "Description 3");
            Todo todo3 = todoService.create(todoCreateRequest3);
            Assertions.assertNotEquals(todo2.getId(),todo3.getId());

        }

        @Test
        public void testReadTodo() {
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("Title", "Description");
            Todo createdTodo = todoService.create(todoCreateRequest);
            Todo readTodo = todoService.read(createdTodo.getId());
            Assertions.assertEquals(createdTodo.getId(), readTodo.getId());
        }

        @Test
        public void testReadNonExistentTodo() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                todoService.read("non-existent-id");
            });
        }
        @Test
        public void testReadWithEmptyId(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> { todoService.read("");});
        }
        @Test
       public void testReadWithNullId(){
            Assertions.assertThrows(IllegalArgumentException.class, () -> { todoService.read(null);});
        }

        @Test
        public void testUpdateTodo() {
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("Title", "Description");
            Todo createdTodo = todoService.create(todoCreateRequest);
            Todo updatedTodo = todoService.update(createdTodo.getId(), true);
            Assertions.assertTrue(updatedTodo.isCompleted());
        }
        @Test
        public void testUpdateWithNullId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.update(null,true);});
        }
    @Test
    public void testUpdateWithEmptyId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.update("",true);});
    }
    @Test
    public void testUpdateWithUnexistingId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.update("abc",true);});
    }
    @Test
    public void testDeleteWithNullId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.delete(null);});
    }
    @Test
    public void testDeleteWithEmptyId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.delete("");});
    }
    @Test
    public void testDeleteWithUnexistingId(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{todoService.delete("ali");});
    }





    @Test
        public void testDeleteTodo() {
            TodoCreateRequest todoCreateRequest = new TodoCreateRequest("Title", "Description");
            Todo createdTodo = todoService.create(todoCreateRequest);
            todoService.delete(createdTodo.getId());
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                todoService.read(createdTodo.getId());
            });
        }

        @Test
        public void testListTodos() {
            TodoCreateRequest todoCreateRequest1 = new TodoCreateRequest("Title 1", "Description 1");
            TodoCreateRequest todoCreateRequest2 = new TodoCreateRequest("Title 2", "Description 2");
            todoService.create(todoCreateRequest1);
            todoService.create(todoCreateRequest2);
            Assertions.assertEquals(2, todoService.list().size());
        }

        @Test
        public void testemptyList(){
        TodoServiceImpl todoService1 = new TodoServiceImpl();
            Assertions.assertEquals(0, todoService1.list().size());

    }

        @Test
        public void testListCompletedTodos() {
            TodoCreateRequest todoCreateRequest1 = new TodoCreateRequest("Title 1", "Description 1");
            TodoCreateRequest todoCreateRequest2 = new TodoCreateRequest("Title 2", "Description 2");
            TodoCreateRequest todoCreateRequest3 = new TodoCreateRequest("Title 3", "Description 3");
            Todo todo1 = todoService.create(todoCreateRequest1);
            Todo todo2 = todoService.create(todoCreateRequest2);
            Todo todo3 = todoService.create(todoCreateRequest3);

            todoService.update(todo1.getId(), true);
            todoService.update(todo3.getId(), true);
           // as todo 2 is not completed yet
            Assertions.assertEquals(2, todoService.listCompleted().size());
        }
    @Test
    public void testEmptyCompletedList(){
        TodoServiceImpl todoService1 = new TodoServiceImpl();
        Assertions.assertEquals(0, todoService1.listCompleted().size());
    }
    }

