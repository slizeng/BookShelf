package com.thoughtworks.sli.controller;

import com.thoughtworks.sli.model.Book;
import com.thoughtworks.sli.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class BookShelfController {

    private final Logger logger = LoggerFactory.getLogger(BookShelfController.class);

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelMap model = new ModelMap();
        model.put("books", bookService.findAll());
        return new ModelAndView("books", model);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelMap model = new ModelMap();
        model.put("books", bookService.findAll());
        return new ModelAndView("books", model);
    }

    @RequestMapping(value = "/book/{isbn}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String isbn) {
        Book book =  bookService.getBookByIsbn(isbn);
        if (book == null){
            return getErrorView("This book is not exist.");
        }
        ModelAndView modelAndView = new ModelAndView("book");
        modelAndView.getModel().put("book", book);
        return modelAndView;
    }

    @RequestMapping(value = "/book/new", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("addBook");
        return modelAndView;
    }

    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public ModelAndView doAdd(Book book) {
        if (bookService.getBookByIsbn(book.getIsbn()) != null){
            return getErrorView("This Book is exist.");
        }
        return saveBook(book);
    }

    @RequestMapping(value = "/book/update/{isbn}", method = RequestMethod.GET)
    public ModelAndView update(@PathVariable String isbn){
        Book book = bookService.getBookByIsbn(isbn);
        if (book == null){
            return getErrorView("This book is not exist.");
        }
        ModelAndView modelAndView = new ModelAndView("addBook");
        return modelAndView;
    }

    @RequestMapping(value = "/book/delete/{isbn}", method = RequestMethod.GET)
    public ModelAndView delete(Book book){
        ModelMap model = new ModelMap();
        bookService.delete(book);
        model.put("books", bookService.findAll());
        return new ModelAndView("books", model);
    }

    @RequestMapping(value = "/undo", method = RequestMethod.GET)
    public ModelAndView undo(Book book){
        ModelMap model = new ModelMap();
        bookService.undo();
        model.put("books", bookService.findAll());
        return new ModelAndView("books", model);
    }

    @RequestMapping(value = "/book/update", method = RequestMethod.POST)
    public ModelAndView doUpdate(Book book) {
        if (bookService.getBookByIsbn(book.getIsbn()) == null){
            return getErrorView("This book is not exist.");
        }
        return saveBook(book);
    }

    private ModelAndView getErrorView(String errorMessage) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.getModel().put("error", errorMessage);
        return modelAndView;
    }


    private ModelAndView saveBook(Book book) {
        ModelAndView modelAndView = new ModelAndView("book");
        modelAndView.getModel().put("book", book);
        bookService.saveBook(book);
        return modelAndView;
    }
}
