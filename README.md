# Student Planner

## Proposal

This application is geared towards helping secondary and post secondary students organize and plan out their daily 
schedules.  As a full-time post secondary student, this project is of interest to me because it would be greatly useful 
to have a dedicated application that I can use to keep track of everything I need to do each day.

**Functionality:**

- To-do lists for every day of the week
- Old lists automatically cycle out for new ones each day
- Due dates associated with each task
- Reminders for tasks that are due soon
- Automatically clears out tasks that are past their due dates
- Work-list view to see tasks grouped by tag

## User Stories

- As a user, I want to be able to have a to-do list for each day of the week
- As a user, I want to be able to view my to-do lists one at a time
- As a user, I want to be able to navigate forward and backward between my to-do lists
- As a user, I want to be able to add a task to any of the to-do lists up to a week ahead
- As a user, I want to be able to remove a task from any to-do list
- As a user, I want to be able to assign a due date to a new task that automatically counts down each day
- As a user, I want to be able to see what tasks are due soon below the current to-do list
- As a user, I want to be able to have tasks that are past their due date automatically cleared out
- As a user, I want to be able to add a tag that can be assigned to new tasks
- As a user, I want to be able to assign a tag to a new task
- As a user, I want to be able to delete a tag
- As a user, I want to be able to view my created tags
- As a user, I want to be able to view a work-list of my tasks grouped by tag
- As a user, I want to be able to save my todo-lists and tags to file
- As a user, I want to be able to load my todo-lists and tags from file

## Phase 4: Task 2
I chose to make my TagList class more robust by having getTag() throw a TagNotFoundException and having addTag() 
throw a TagAlreadyExistsException.  These exceptions are thrown when called in other methods such as Planner.createTag()
and Planner.createTask(), both of which are called in the console UI and GUI classes.  Tests for these exceptions can 
be found in PlannerTest and TagListTest.

## Phase 4: Task 3
If I had more time, I would have liked to create more helper functions in the Planner class to improve readability and
reduce dependency/coupling.  In terms of the class hierarchy, I'm happy with the current design and with how everything 
is organized.
