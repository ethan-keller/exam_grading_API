package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class Mocks the UserRepository interface for testing purposes.
 *
 */
public class MockRepository implements UserRepository {

  private transient List<User> students;

  private transient List<User> teachers;

  private transient User user1;

  private transient User user2 ;

  public MockRepository() {
    this.students = new ArrayList<>();
    this.teachers = new ArrayList<>();
    this.user1 = new User("student1","pass",0);
    this.user2 = new User("teacher1","pass",1);
    students.add(user1);
    teachers.add(user2);
  }


  @Override
  public List<User> getAllUsers() {
    return students;
  }

  @Override
  public User getUserByNetId(String netId) {
    if (netId.equals("student1")){
      return user1;
    }
    else {
      return null;
    }
  }

  @Override
  public User getUserByNetIdAndPassword(String netId, String password) {
    if (netId.equals("student1") && password.equals("pass")){
      return user1;
    }
    else {
      return null;
    }
  }

  @Override
  public List<User> getUsersOfType(int type) {
    int student = 0;
    if (type == student){
      return students;
    }
    int teacher = 1;
    if (type == teacher){
      return teachers;
    }
      return null;
  }

  @Override
  public void insertUser(String netId, String password, int type) {
  }

  @Override
  public void updateUser(String netId, String password, int type) {

  }

  @Override
  public void deleteUser(String netId) {

  }

  @Override
  public List<User> findAll() {
    return null;
  }

  @Override
  public List<User> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public List<User> findAllById(Iterable<String> strings) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {

  }

  @Override
  public void delete(User entity) {

  }

  @Override
  public void deleteAll(Iterable<? extends User> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends User> S save(S entity) {
    return null;
  }

  @Override
  public <S extends User> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<User> findById(String s) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends User> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public void deleteInBatch(Iterable<User> entities) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public User getOne(String s) {
    return null;
  }

  @Override
  public <S extends User> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends User> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends User> boolean exists(Example<S> example) {
    return false;
  }
}

