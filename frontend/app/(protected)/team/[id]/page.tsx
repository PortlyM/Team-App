import { getTeamById } from "@/lib/api";
import Link from "next/link";
import { createListAction, deleteListAction, createTaskAction } from "../actions";
import TaskItem from "./TaskItem";

export default async function TeamPage({ params }: { params: Promise<{ id: string }> }) {
    const { id } = await params;
    const team = await getTeamById(id);

    return (
        <div className="h-full flex flex-col">
            {/* Header */}
            <div className="p-6 border-b border-gray-200 bg-white flex items-center gap-4 shrink-0">
                <Link 
                    href="/team"
                    className="p-2 rounded-full hover:bg-gray-100 transition-colors text-gray-600"
                >
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                    </svg>
                </Link>
                <h1 className="text-2xl font-bold text-gray-800">{team.name}</h1>
            </div>

            {/* Content */}
            <div className="flex-1 flex overflow-hidden">
                {/* Left Column: Members */}
                <div className="w-80 border-r border-gray-200 bg-gray-50 flex flex-col shrink-0 overflow-y-auto">
                    <div className="p-6">
                        <h2 className="text-lg font-semibold text-gray-700 mb-4">Members ({team.members.length})</h2>
                        <ul className="space-y-3">
                            {team.members.map(member => (
                                <li key={member.id} className="flex items-center gap-3 bg-white p-3 rounded-lg border border-gray-200 shadow-sm">
                                    <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold">
                                        {member.name.charAt(0).toUpperCase()}
                                    </div>
                                    <div className="overflow-hidden">
                                        <p className="font-medium text-gray-800 truncate">{member.name}</p>
                                        <p className="text-xs text-gray-500 truncate">{member.email}</p>
                                    </div>
                                    {team.leader?.id === member.id && (
                                        <span className="ml-auto text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full font-medium">Leader</span>
                                    )}
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

                {/* Right Column: Todo Lists */}
                <div className="flex-1 bg-white p-6 overflow-y-auto">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-xl font-bold text-gray-800">Todo Lists</h2>
                        <form action={createListAction.bind(null, team.id)} className="flex gap-2">
                            <input 
                                type="text" 
                                name="name" 
                                placeholder="New list name..." 
                                required
                                className="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-blue-500 focus:border-blue-500 outline-none"
                            />
                            <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">
                                Add List
                            </button>
                        </form>
                    </div>

                    <div className="space-y-8 pb-12">
                        {team.todoLists.length === 0 ? (
                            <div className="text-center py-12 text-gray-500 border-2 border-dashed border-gray-200 rounded-xl">
                                No todo lists yet. Create one above!
                            </div>
                        ) : (
                            team.todoLists.map(list => (
                                <div key={list.id} className="bg-gray-50 rounded-xl border border-gray-200 p-6">
                                    <div className="flex items-center justify-between mb-4">
                                        <h3 className="text-lg font-bold text-gray-800">{list.name}</h3>
                                        <div className="flex items-center gap-3">
                                            <form action={createTaskAction.bind(null, team.id, list.id)} className="flex gap-2">
                                                <input 
                                                    type="text" 
                                                    name="content" 
                                                    placeholder="Task description..." 
                                                    required
                                                    className="border border-gray-300 rounded-lg px-3 py-1.5 text-sm focus:ring-blue-500 focus:border-blue-500 outline-none"
                                                />
                                                <button type="submit" className="bg-green-500 hover:bg-green-600 text-white px-3 py-1.5 rounded-lg text-sm font-medium transition-colors">
                                                    Add Task
                                                </button>
                                            </form>
                                            <form action={deleteListAction.bind(null, team.id, list.id)}>
                                                <button type="submit" className="bg-white border border-red-200 hover:bg-red-50 text-red-600 px-3 py-1.5 rounded-lg text-sm font-medium transition-colors">
                                                    Delete List
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                    
                                    <div className="space-y-3">
                                        {list.tasks.length === 0 ? (
                                            <p className="text-sm text-gray-500 italic">No tasks in this list.</p>
                                        ) : (
                                            list.tasks.map(task => (
                                                <TaskItem key={task.id} teamId={team.id} task={task} />
                                            ))
                                        )}
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
