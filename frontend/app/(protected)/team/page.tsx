import { getAllTeams, getUserId } from "@/lib/api";
import Link from "next/link";
import { joinTeamAction, leaveTeamAction, createTeamAction } from "./actions";

export default async function TeamsPage() {
    const teams = await getAllTeams();
    const userId = await getUserId();

    return (
        <div className="p-8">
            <div className="flex items-center justify-between mb-8">
                <h1 className="text-3xl font-bold text-gray-800">Teams</h1>
                <form action={createTeamAction} className="flex gap-2">
                    <input 
                        type="text" 
                        name="name" 
                        placeholder="New team name..." 
                        required
                        className="border border-gray-300 rounded-lg px-4 py-2 text-sm focus:ring-blue-500 focus:border-blue-500 outline-none"
                    />
                    <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors">
                        Create Team
                    </button>
                </form>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {teams.map(team => {
                    const isMember = team.members.some(m => m.id === userId);

                    return (
                        <div key={team.id} className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden flex flex-col hover:shadow-md transition-shadow">
                            <Link href={isMember ? `/team/${team.id}` : '#'} className={`p-6 flex-1 ${!isMember && 'cursor-default'}`}>
                                <h2 className="text-xl font-semibold text-gray-800">{team.name}</h2>
                                <p className="text-sm text-gray-500 mt-2">
                                    {team.members.length} member{team.members.length !== 1 ? 's' : ''}
                                </p>
                            </Link>
                            <div className="px-6 py-4 bg-gray-50 border-t border-gray-100">
                                {isMember ? (
                                    <form action={leaveTeamAction.bind(null, team.id)}>
                                        <button className="w-full py-2 px-4 rounded-lg bg-white border border-red-200 text-red-600 font-medium hover:bg-red-50 transition-colors">
                                            Leave Team
                                        </button>
                                    </form>
                                ) : (
                                    <form action={joinTeamAction.bind(null, team.id)}>
                                        <button className="w-full py-2 px-4 rounded-lg bg-green-500 text-white font-medium hover:bg-green-600 transition-colors shadow-sm">
                                            Join Team
                                        </button>
                                    </form>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>
            {teams.length === 0 && (
                <div className="text-center py-12 text-gray-500">
                    No teams available yet.
                </div>
            )}
        </div>
    );
}
