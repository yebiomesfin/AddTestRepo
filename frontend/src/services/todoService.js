import axios from 'axios'

const BASE_URL = '/api/todos'

export const getTodos = async () => {
  const response = await axios.get(BASE_URL)
  return response.data
}

export const getTodoById = async (id) => {
  const response = await axios.get(`${BASE_URL}/${id}`)
  return response.data
}

export const createTodo = async (todo) => {
  const response = await axios.post(BASE_URL, todo)
  return response.data
}

export const updateTodo = async (id, todo) => {
  const response = await axios.put(`${BASE_URL}/${id}`, todo)
  return response.data
}

export const deleteTodo = async (id) => {
  await axios.delete(`${BASE_URL}/${id}`)
}
