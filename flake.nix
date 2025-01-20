{
  description = "BTB";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/release-24.11";
  };

  outputs = {
    self,
    nixpkgs,
  }: let
    system = "x86_64-linux";
    pkgs = import nixpkgs {inherit system;};
  in {
    devShells.${system} = {
      default = pkgs.mkShell {
        buildInputs = with pkgs; [openjdk];
        COMPOSE_FILE = "docker-compose.dev.yaml";
      };
      prod = pkgs.mkShell {
        buildInputs = with pkgs; [openjdk];
        COMPOSE_FILE = "docker-compose.prod.yaml";
      };
    };
  };
}
