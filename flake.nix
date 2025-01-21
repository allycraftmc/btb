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
    btb-prod-upload = pkgs.writeShellScriptBin "btb-prod-upload" (builtins.readFile ./scripts/prod-upload.sh);
  in {
    devShells.${system} = {
      default = pkgs.mkShell {
        buildInputs = with pkgs; [openjdk btb-prod-upload];
      };
    };
  };
}
