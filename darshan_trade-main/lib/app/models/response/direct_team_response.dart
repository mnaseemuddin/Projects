import 'dart:convert';

List<DirectTeamResponse> directTeamResponseFromJson(String str) => List<DirectTeamResponse>.from(json.decode(str).map((x) => DirectTeamResponse.fromJson(x)));

String directTeamResponseToJson(List<DirectTeamResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class DirectTeamResponse {
  DirectTeamResponse({
    required this.username,
    required this.name,
    required this.status,
    required this.uuid,
    required this.rank,
  });

  final String username;
  final String name;
  final String status;
  final String uuid;
  final String rank;

  factory DirectTeamResponse.fromJson(Map<String, dynamic> json) => DirectTeamResponse(
    username: json["username"],
    name: json["name"],
    status: json["status"],
    uuid: json["uuid"],
    rank: json["rank"],
  );

  Map<String, dynamic> toJson() => {
    "username": username,
    "name": name,
    "status": status,
    "uuid": uuid,
    "rank": rank,
  };
}
