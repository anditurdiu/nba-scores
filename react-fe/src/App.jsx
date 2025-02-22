import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Basketball } from "lucide-react";

const App = () => {
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchContent = async () => {
      try {
        // Replace this URL with your actual endpoint
        const response = await fetch(
          "https://api.example.com/basketball-content"
        );
        const data = await response.json();
        setContent(data.htmlContent);
        setLoading(false);
      } catch (err) {
        setError("Failed to load content. Please try again later.");
        setLoading(false);
      }
    };

    fetchContent();
  }, []);

  return (
    <div className="min-h-screen bg-orange-50 p-8">
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="flex items-center justify-center mb-8 space-x-4">
          <Basketball className="w-12 h-12 text-orange-600" />
          <h1 className="text-4xl font-bold text-orange-900">
            Basketball Zone
          </h1>
        </div>

        {/* Main Content */}
        <Card className="shadow-lg">
          <CardHeader>
            <CardTitle className="text-2xl text-orange-800">
              Latest Basketball News
            </CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="flex items-center justify-center p-8">
                <div className="w-8 h-8 border-4 border-orange-600 border-t-transparent rounded-full animate-spin"></div>
              </div>
            ) : error ? (
              <div className="text-red-600 p-4 text-center">{error}</div>
            ) : (
              <div
                className="prose max-w-none"
                dangerouslySetInnerHTML={{
                  __html: content || "No content available",
                }}
              />
            )}
          </CardContent>
        </Card>

        {/* Footer */}
        <div className="mt-8 text-center text-orange-700">
          <p>Stay updated with the latest basketball news and highlights!</p>
        </div>
      </div>
    </div>
  );
};

export default App;
