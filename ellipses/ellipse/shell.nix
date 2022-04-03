{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    gnumake
    pkg-config
    boost
    (opencv.override { enableGtk3 = true; })
    cmake

    hotspot

    # keep this line if you use bash
    bashInteractive
  ];
}
