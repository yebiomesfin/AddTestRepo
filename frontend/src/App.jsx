import { useState, useEffect } from 'react'
import TodoList from './components/TodoList'
import TodoForm from './components/TodoForm'
import { getTodos, createTodo, updateTodo, deleteTodo } from './services/todoService'
import './App.css'

function App() {
  const [todos, setTodos] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchTodos()
  }, [])

  const fetchTodos = async () => {
    try {
      setLoading(true)
      const data = await getTodos()
      setTodos(data)
    } catch (err) {
      setError('Failed to load todos')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (title) => {
    try {
      const newTodo = await createTodo({ title, completed: false })
      setTodos((prev) => [...prev, newTodo])
    } catch (err) {
      setError('Failed to create todo')
    }
  }

  const handleToggle = async (todo) => {
    try {
      const updated = await updateTodo(todo.id, { ...todo, completed: !todo.completed })
      setTodos((prev) => prev.map((t) => (t.id === updated.id ? updated : t)))
    } catch (err) {
      setError('Failed to update todo')
    }
  }

  const handleDelete = async (id) => {
    try {
      await deleteTodo(id)
      setTodos((prev) => prev.filter((t) => t.id !== id))
    } catch (err) {
      setError('Failed to delete todo')
    }
  }

  return (
    <div className="app">
      <h1>Todo App</h1>
      {error && <p className="error">{error}</p>}
      <TodoForm onSubmit={handleCreate} />
      {loading ? (
        <p>Loading...</p>
      ) : (
        <TodoList todos={todos} onToggle={handleToggle} onDelete={handleDelete} />
      )}
    </div>
  )
}

export default App
