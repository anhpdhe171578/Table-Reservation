export default function UserTable({ users, setEditingUser, handleDelete }) {
  return (
    <table className="w-full border-collapse border border-gray-300 mt-4">
      <thead>
        <tr className="bg-gray-200">
          <th className="border p-2">ID</th>
          <th className="border p-2">Username</th>
          <th className="border p-2">Email</th>
          <th className="border p-2">Actions</th>
        </tr>
      </thead>
      <tbody>
        {users.map((u) => (
          <tr key={u.id}>
            <td className="border p-2">{u.id}</td>
            <td className="border p-2">{u.username}</td>
            <td className="border p-2">{u.email}</td>
            <td className="border p-2 space-x-2">
              <button
                className="bg-yellow-400 px-2 py-1 rounded"
                onClick={() => setEditingUser(u)}
              >
                Edit
              </button>
              <button
                className="bg-red-500 text-white px-2 py-1 rounded"
                onClick={() => handleDelete(u.id)}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
