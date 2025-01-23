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
    btb-clean-world = pkgs.stdenv.mkDerivation {
      name = "btb-clean-world";
      src = ./scripts/clean-world.sh;
      phases = "buildPhase";
      nativeBuildInputs = [pkgs.makeWrapper];
      buildPhase = ''
        mkdir -p $out/bin
        cp $src $out/bin/btb-clean-world
        chmod +x $out/bin/btb-clean-world
        substituteInPlace $out/bin/btb-clean-world --replace-fail "#!/usr/bin/env bash" "#!${pkgs.bash}/bin/bash"
        wrapProgram $out/bin/btb-clean-world --set PATH "${pkgs.lib.makeBinPath [pkgs.coreutils pkgs.mcaselector]}"
      '';
    };
  in {
    devShells.${system} = {
      default = pkgs.mkShell {
        buildInputs = with pkgs; [openjdk btb-prod-upload btb-clean-world];
      };
    };
    packages.${system}.btb-clean-world = btb-clean-world;
  };
}
