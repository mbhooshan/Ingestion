/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skry.ingestion;

import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.BlocksDownloadedEventListener;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;

import com.google.common.net.InetAddresses;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.Future;

/**
 * Downloads the block given a block hash from the localhost node and prints it out.
 */
public class FetchBlock {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        System.out.println("Connecting to node");
        final NetworkParameters params = TestNet3Params.get();

//        File chainFile = new File("seed.spvchain");
//        SPVBlockStore chainStore = new SPVBlockStore(params, chainFile);
//        BlockChain chain = new BlockChain(params, chainStore);
//        PeerGroup peerGroup = new PeerGroup(params, chain);
//        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, blockStore);
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        
       /* DownloadProgressTracker bListener = new DownloadProgressTracker() {
            @Override
            public void doneDownload() {
                System.out.println("blockchain downloaded");
            }
        };*/
        
        peerGroup.start();
//        peerGroup.startBlockChainDownload(bListener);
//        bListener.await();


        // shutting down again
//        peerGroup.stop();
        
//        PeerAddress addr = new PeerAddress(InetAddress.getLocalHost(), params.getPort());
//        PeerAddress addr = new PeerAddress(InetAddress.getByAddress("192.168.1.121", "8333".getBytes()));
//        peerGroup.addAddress(addr);
        peerGroup.waitForPeers(1).get();
        Peer peer = peerGroup.getConnectedPeers().get(0);
        Sha256Hash blockHash = Sha256Hash.wrap("0000000000001386495b6b32a3e4ef1c3a05389b896d3c87e3ff4268522e3f01");
//        Sha256Hash blockHash = Sha256Hash.wrap("000203232d");
        Future<Block> future = peer.getBlock(blockHash);
        
System.out.println("heaight...."+peer.getBestHeight());

        
//        peerGroup.downloadBlockChain();
        System.out.println("Waiting for node to send us the requested block: " + blockHash);
        Block block = future.get();
        
//        System.out.println("Block:- "+block);
//        System.out.println("Chain heah :"+chain.getChainHead().toString());
        
        /*System.out.println("Finding parent block");
        Sha256Hash PrevBlockHash = block.getPrevBlockHash();
        Sha256Hash blockHash1 = Sha256Hash.wrap(PrevBlockHash.getBytes());
        Future<Block> future1 = peer.getBlock(blockHash1);
        Block blk = future1.get();
        System.out.println("blk:\n"+blk);
        System.out.println("Heigh of the block is :"+ blk.getDifficultyTarget());
        System.out.println("Time :" + blk.getTime());*/
    }
}
