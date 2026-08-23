import { getAllTeams, getAllUsers, getUserId, getUser } from "@/lib/api";

export default async function DashboardPage() {
    const userId = await getUserId();
    if (!userId) return null;

    const [user, users, teams] = await Promise.all([
        getUser(userId),
        getAllUsers(),
        getAllTeams()
    ]);

    const totalUsers = users.length;
    const totalTeams = teams.length;
    
    // Sum all lists from all teams
    const totalLists = teams.reduce((acc, team) => acc + team.todoLists.length, 0);

    return (
        <div className="h-full flex flex-col items-center justify-center bg-gray-50 p-8">
            <div className="bg-white rounded-2xl shadow-xl border border-gray-200 p-12 max-w-2xl w-full text-center">
                <h1 className="text-4xl font-extrabold text-gray-900 mb-2">
                    Welcome, {user.name}!
                </h1>
                <p className="text-gray-500 mb-10 text-lg">Here's a quick overview of the application</p>
                
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-blue-50 rounded-xl p-6 border border-blue-100 flex flex-col items-center justify-center">
                        <span className="text-4xl font-black text-blue-600 mb-2">{totalUsers}</span>
                        <span className="text-blue-900 font-medium text-sm uppercase tracking-wide">Total Users</span>
                    </div>
                    
                    <div className="bg-green-50 rounded-xl p-6 border border-green-100 flex flex-col items-center justify-center">
                        <span className="text-4xl font-black text-green-600 mb-2">{totalTeams}</span>
                        <span className="text-green-900 font-medium text-sm uppercase tracking-wide">Total Teams</span>
                    </div>
                    
                    <div className="bg-purple-50 rounded-xl p-6 border border-purple-100 flex flex-col items-center justify-center">
                        <span className="text-4xl font-black text-purple-600 mb-2">{totalLists}</span>
                        <span className="text-purple-900 font-medium text-sm uppercase tracking-wide">Total Lists</span>
                    </div>
                </div>
            </div>
        </div>
    );
}