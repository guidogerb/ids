# Identity Streams (IDS)
GuidoGerb Publishing, LLC

This is research and development reducing the bigdata footprint.

IDS is a system of globally distributed symbol tables.

A simplified representation of a symbol table entry (or simply, a symbol) in Java has the following format: <symbol
name (identifier), type, scope, [attributes]>. Given a global variable declaration like final double ratio; the
corresponding symbol would then be <ratio, double, global, [final]>.

This is also an argument for a 7th normal form (7NF) in data, representing a strategy for using these symbol tables to
serialized data as nothing more than Identity Streams (IDS) where distributed symbol tables are used to compress all
information into its series of identities.

A management information base (MIB) is a database used for managing the entities in a communication network.

Objects in the MIB are defined using a subset of Abstract Syntax Notation One (ASN.1)
called "Structure of Management Information Version 2 (SMIv2)" RFC 2578. This software performs the parsing, and is an
MIB compiler.

Abstract Syntax Notation One (ASN.1) is a standard and flexible notation that describes data structures for
representing, encoding, transmitting, and decoding data.
It provides a set of formal rules for describing the structure of objects that are
independent of machine-specific encoding techniques and is a precise, formal notation
that removes ambiguities.

The MIB hierarchy can be depicted as a tree with a nameless root, the levels of which
are assigned by different organizations. The top-level MIB OIDs belong to different
standards organizations, while lower-level object IDs are allocated by associated
organizations. This model permits management across all layers of the OSI reference model,
extending into applications such as databases, email, and the Java reference model,
as MIBs can be defined for all such area-specific information and operations.

There are a large number of MIBs defined by standards organizations like the IETF,
private enterprises and other entities. This project is an attempt to standardize the
streaming of character sets in a compressed blocks, Object IDs, representing object
identifiers allocated according to the rules specified in ASN.1 with bounded
precision in the Structure of Management Information (SMI).

See MIB Index - http://www.icir.org/fenner/mibs/mib-index.html
See Definitions of Managed Objects for Character Stream Devices using SMIv2 - https://tools.ietf.org/html/rfc1658
See Character Sets - https://www.iana.org/assignments/character-sets/character-sets.xml

unicode 13.0.0
character encoding utf-8, utf-16 and utf-32
